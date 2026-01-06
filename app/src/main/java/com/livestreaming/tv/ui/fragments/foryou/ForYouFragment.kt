package com.livestreaming.tv.ui.fragments.foryou

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.ForYouAdapter
import com.livestreaming.tv.adapter.ImageSliderAdapter
import com.livestreaming.tv.databinding.FragmentForYouBinding
import com.livestreaming.tv.utils.HeroItem
import com.livestreaming.tv.utils.contentPosters
import com.livestreaming.tv.utils.dp
import com.livestreaming.tv.utils.heroItems
import com.livestreaming.tv.utils.scrollToTop
import com.livestreaming.tv.utils.toast
import com.livestreaming.tv.utils.visibleIf
import com.livestreaming.tv.viewmodels.TabBarViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForYouFragment : Fragment() {

    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

    private val autoSlide = object : Runnable {
        override fun run() {
            currentIndex = (currentIndex + 1) % heroItems.size
            binding.vpBackgroundSlider.setCurrentItem(currentIndex, true)
            handler.postDelayed(this, 4000)
        }
    }

    private val tabBarViewModel: TabBarViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabBarViewModel.scrollToTop.observe(viewLifecycleOwner) {
            scrollToTop(binding.scrollView)
        }

        binding.root.requestFocus()
        setupSlider()
        setupRecyclerViews()
        setupKeyHandling()
        backpressHandler()
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

        binding.btnWatchNow.visibleIf(!item.showButtons)
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

    private fun setupRecyclerViews() {
        binding.btnWatchNow.setOnFocusChangeListener { v, hasFocus ->
            v.animate().scaleX(if (hasFocus) 1.1f else 1f).scaleY(if (hasFocus) 1.1f else 1f)
                .setDuration(150).start()
        }

        binding.rvTopMovies.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ForYouAdapter(contentPosters) { positon, item ->
                toast("${positon} ${item.img.toString()}")
            }
        }

        binding.rvUpcomingMovies.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ForYouAdapter(contentPosters) { positon, item ->
                toast("${positon} ${item.img.toString()}")

            }
        }
    }

    private fun setupKeyHandling() {
        with(binding) {
            // Root must be focusable to receive DPAD events
            btnWatchNow.requestFocus()

            btnWatchNow.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    btnWatchNow.apply {
                        strokeWidth = 1
                    }
                } else {
                    btnWatchNow.strokeWidth = 0
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlide)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoSlide, 4000)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(autoSlide)
        _binding = null
        super.onDestroyView()
    }

    private fun backpressHandler() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.forYouFragment) {
                requireActivity().finishAffinity()
            } else {
                findNavController().popBackStack()
            }
        }
    }

}


