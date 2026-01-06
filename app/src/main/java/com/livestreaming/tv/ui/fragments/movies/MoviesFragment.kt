package com.livestreaming.tv.ui.fragments.movies

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.MovieCategoryAdapter
import com.livestreaming.tv.adapter.MoviePagerAdapter
import com.livestreaming.tv.adapter.MovieShowImageAdapter
import com.livestreaming.tv.adapter.TVShowChipAdapter
import com.livestreaming.tv.databinding.FragmentMoviesBinding
import com.livestreaming.tv.utils.Movie
import com.livestreaming.tv.utils.contentPosters
import com.livestreaming.tv.utils.dp
import com.livestreaming.tv.utils.dummyMovies
import com.livestreaming.tv.utils.dummyMoviesCategory
import com.livestreaming.tv.utils.goneIf
import com.livestreaming.tv.utils.log
import com.livestreaming.tv.utils.scrollToTop
import com.livestreaming.tv.viewmodels.TabBarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

    private lateinit var sliderAdapter: MoviePagerAdapter
    private lateinit var categoryChipAdapter: TVShowChipAdapter
    private lateinit var movieCategoryAdapter: MovieCategoryAdapter
    private lateinit var movieShowImageAdapter: MovieShowImageAdapter

    private val tabBarViewModel: TabBarViewModel by activityViewModels()

    private val MAX_STAR = 5

    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            if (dummyMovies.isEmpty()) return
            
            // Check if binding is still valid before accessing it
            if (_binding == null) return

            currentIndex = (currentIndex + 1) % dummyMovies.size
            binding.vpMovies.setCurrentItem(currentIndex, true)
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabBarViewModel.scrollToTop.observe(viewLifecycleOwner) {
            scrollToTop(binding.scrollView)
        }

        setupSlider()
        setupHeroButtonsFocus()
        setupBackgroundImages()
        setupCategory()
        setupRecyclerViews()
    }

    //  SLIDER
    private fun setupSlider() {
        sliderAdapter = MoviePagerAdapter().apply {
            moviesList.submitList(dummyMovies)
        }

        binding.vpMovies.apply {
            adapter = sliderAdapter
            offscreenPageLimit = 1
            setPageTransformer(CenterScaleTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentIndex = position % dummyMovies.size
                    sliderAdapter.updateSelected(currentIndex)
                    updateIndicators(currentIndex)
                    updateHeroContent(dummyMovies[currentIndex])

                    post {
                        val recycler = getChildAt(0) as RecyclerView
                        recycler.findViewHolderForAdapterPosition(position)?.itemView?.requestFocus()
                    }
                }
            })
        }

        setupIndicators()
        updateHeroContent(dummyMovies.first())
        handler.postDelayed(autoSlideRunnable, 4000)
    }

    //  HERO CONTENT
    private fun updateHeroContent(item: Movie) {
        with(binding) {
            tvHeroTitle.text = item.title
            tvHeroMetadata.text = item.description
            tvHeroGenres.text = item.genre
            tvHeroDescription.text = item.description

            btnWatchNow.goneIf(item.poster == R.drawable.slid_1)
            btnAdd.goneIf(item.poster == R.drawable.slid_1)

            starContainer.orientation = HORIZONTAL
            setRating(5f)
        }
    }

    // STAR RATING
    fun setRating(imdbRating: Float) {
        binding.starContainer.removeAllViews()

        val rating = imdbRating / 2f
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5f

        for (i in 0 until MAX_STAR) {
            val star = ImageView(context)

            star.setImageResource(
                when {
                    i < fullStars -> R.drawable.ic_fill_star
                    i == fullStars && hasHalfStar -> R.drawable.ic_unselected_star
                    else -> R.drawable.ic_unselected_star
                }
            )

            val size = resources.displayMetrics.density * 20
            star.layoutParams = LinearLayout.LayoutParams(size.toInt(), size.toInt()).apply {
                marginEnd = 4
            }

            binding.starContainer.addView(star)
        }
    }

    //  INDICATORS
    private fun setupIndicators() {
        binding.llLineIndicators.removeAllViews()

        repeat(dummyMovies.size) {
            val view = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    requireContext().dp(24), requireContext().dp(4)
                ).apply {
                    setMargins(requireContext().dp(4), 0, requireContext().dp(4), 0)
                }
                setBackgroundResource(R.drawable.indicator_inactive)
            }
            binding.llLineIndicators.addView(view)
        }
    }

    private fun updateIndicators(activeIndex: Int) {
        for (i in 0 until binding.llLineIndicators.childCount) {
            binding.llLineIndicators.getChildAt(i).setBackgroundResource(
                if (i == activeIndex) R.drawable.indicator_active
                else R.drawable.indicator_inactive
            )
        }
    }

    //  BACKGROUND IMAGE
    private fun setupBackgroundImages() {
        sliderAdapter.onBackgroundImageItem = { movie ->
            binding.ivBgImage.postDelayed({
                // Check if binding is still valid before accessing it
                _binding?.ivBgImage?.setImageResource(movie.background)
            }, if (binding.ivBgImage.drawable == null) 0 else 1000)
        }
    }

    //  BUTTON FOCUS
    private fun setupHeroButtonsFocus() {
        with(binding) {

            btnWatchNow.setOnFocusChangeListener { _, hasFocus ->
                btnWatchNow.strokeWidth = if (hasFocus) 1 else 0
            }

            btnTrailer.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    btnTrailer.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                    btnTrailer.setTextColor(Color.BLACK)
                } else {
                    btnTrailer.strokeWidth = 1
                    btnTrailer.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white_9_percent)
                    )
                    btnTrailer.setTextColor(Color.WHITE)
                }
            }

            btnAdd.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    btnAdd.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                    ivPlush.setImageResource(R.drawable.ic_add_black)
                } else {
                    btnAdd.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white_9_percent)
                    )
                    ivPlush.setImageResource(R.drawable.ic_add_white)
                }
            }
        }
    }

    //  CATEGORY & LISTS
    private fun setupCategory() {
        categoryChipAdapter = TVShowChipAdapter().apply {
            chipsList.submitList(dummyMoviesCategory)
            onItemClick = { position, name ->
                log("Clicked: $position - $name")
            }
        }
    }

    private fun setupRecyclerViews() {
        movieCategoryAdapter = MovieCategoryAdapter().apply {
            categoryList.submitList(dummyMoviesCategory)
        }

        movieShowImageAdapter = MovieShowImageAdapter().apply {
            imgList.submitList(contentPosters)
        }

        binding.rvCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = movieCategoryAdapter
        }

        binding.rvTopMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = movieShowImageAdapter
        }

        movieShowImageAdapter.onClick = {
            findNavController().navigate(
                MoviesFragmentDirections.actionMoviesFragmentToMoviesHomeScreenFragment(
                    it
                )
            )
        }
    }

    //  PAGE TRANSFORMER
    inner class CenterScaleTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val absPos = abs(position)
            page.scaleX = 1f - (absPos * 0.15f)
            page.scaleY = 1f - (absPos * 0.15f)
            page.alpha = 1f - (absPos * 0.4f)
        }
    }

    //  CLEANUP
    override fun onDestroyView() {
        handler.removeCallbacks(autoSlideRunnable)
        _binding = null
        super.onDestroyView()
    }
}
