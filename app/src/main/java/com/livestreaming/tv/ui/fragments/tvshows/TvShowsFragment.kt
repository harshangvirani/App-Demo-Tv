package com.livestreaming.tv.ui.fragments.tvshows

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.ImageSliderAdapter
import com.livestreaming.tv.adapter.TVShowChipAdapter
import com.livestreaming.tv.adapter.TvShowImageAdapter
import com.livestreaming.tv.databinding.FragmentTvShowsBinding
import com.livestreaming.tv.utils.HeroItem
import com.livestreaming.tv.utils.categoryList
import com.livestreaming.tv.utils.contentPosters
import com.livestreaming.tv.utils.dp
import com.livestreaming.tv.utils.heroItems
import com.livestreaming.tv.utils.log
import com.livestreaming.tv.utils.scrollToTop
import com.livestreaming.tv.viewmodels.TabBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsFragment : Fragment() {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!

    private var categoryName: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private var tvShowImageAdapter: TvShowImageAdapter? = null
    private var tvShowChipAdapter: TVShowChipAdapter? = null

    private val autoSlide = object : Runnable {
        override fun run() {
            currentIndex = (currentIndex + 1) % heroItems.size
            binding.vpBackgroundSlider.setCurrentItem(currentIndex, true)
            handler.postDelayed(this, 4000)
        }
    }

    private val tabBarViewModel: TabBarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTvShowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabBarViewModel.scrollToTop.observe(viewLifecycleOwner) {
            scrollToTop(binding.scrollView)
        }
        setupSlider()
        setupRecyclerView()
    }

    private fun setupSlider() {
        binding.vpBackgroundSlider.adapter = ImageSliderAdapter(heroItems)

        binding.vpBackgroundSlider.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateHeroContent(heroItems[position])
                    updateIndicators(position)
                }
            }
        )

        setupIndicators()
        updateHeroContent(heroItems[0])
        handler.postDelayed(autoSlide, 4000)
    }

    private fun updateHeroContent(item: HeroItem) {
        binding.tvHeroTitle.text = item.title
        binding.tvHeroMetadata.text = item.metadata
        binding.tvHeroGenres.text = item.genres
        binding.tvHeroDescription.text = item.description

        binding.btnWatchNow.visibility =
            if (item.showButtons) View.VISIBLE else View.GONE
        binding.btnMyList.visibility =
            if (item.showButtons) View.VISIBLE else View.GONE
    }

    private fun setupIndicators() {
        binding.llLineIndicators.removeAllViews()
        repeat(heroItems.size) {
            val view = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    requireContext().dp(24),
                    requireContext().dp(4)
                ).apply {
                    setMargins(requireContext().dp(4), 0, requireContext().dp(4), 0)
                }

                setBackgroundResource(R.drawable.indicator_inactive)
            }
            binding.llLineIndicators.addView(view)
        }
    }

    private fun updateIndicators(active: Int) {
        for (i in 0 until binding.llLineIndicators.childCount) {
            binding.llLineIndicators.getChildAt(i)
                .setBackgroundResource(
                    if (i == active)
                        R.drawable.indicator_active
                    else
                        R.drawable.indicator_inactive
                )
        }
    }

    private fun setupRecyclerView() {
        tvShowChipAdapter = TVShowChipAdapter()
        tvShowImageAdapter = TvShowImageAdapter()
        tvShowChipAdapter?.chipsList?.submitList(categoryList.toMutableList())
        tvShowImageAdapter?.imgList?.submitList(contentPosters)
        with(binding) {
            rvCategory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = tvShowChipAdapter
            }
            tvShowChipAdapter?.onItemClick = { position, item ->
                log("${position}+ ${item}")
            }
            if (categoryName?.isEmpty() == true) {
                categoryName = categoryList.get(0).toString()
            }
            if (categoryName.equals("Crime")) {
                tvShowImageAdapter?.imgList?.submitList(contentPosters.take(6))
                tvShowImageAdapter?.notifyDataSetChanged()
            } else if (categoryName.equals("Adventure")) {
                tvShowImageAdapter?.imgList?.submitList(contentPosters.take(8))
                tvShowImageAdapter?.notifyDataSetChanged()

            }

            rvTopMovies.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = tvShowImageAdapter
            }
            tvShowImageAdapter?.onClick = { position, item ->
                findNavController().navigate(TvShowsFragmentDirections.actionTvShowsFragmentToTvHomeScreenFragment())
            }
        }
    }

}