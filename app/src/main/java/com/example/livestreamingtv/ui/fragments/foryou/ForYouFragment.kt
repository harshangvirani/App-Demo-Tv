package com.example.livestreamingtv.ui.fragments.foryou

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.livestreamingtv.R
import com.example.livestreamingtv.adapter.ForYouAdapter
import com.example.livestreamingtv.adapter.ImageSliderAdapter
import com.example.livestreamingtv.databinding.FragmentForYouBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!
    private lateinit var sliderAdapter: ImageSliderAdapter
    private lateinit var latestReleasesAdapter: ForYouAdapter
    private lateinit var trendingAdapter: ForYouAdapter
    private lateinit var continueWatchingAdapter: ForYouAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    
    // Sample background images (using same image multiple times - replace with actual images)
    private val sliderImages = listOf(
        R.drawable.bg_image,
        R.drawable.bg_image,
        R.drawable.bg_image,
        R.drawable.bg_image,
        R.drawable.bg_image
    )
    
    // Sample content posters
    private val contentPosters = listOf(
        R.drawable.wednesday_poster,
        R.drawable.wednesday_poster,
        R.drawable.wednesday_poster,
        R.drawable.wednesday_poster,
        R.drawable.wednesday_poster
    )
    
    private val sliderRunnable = object : Runnable {
        override fun run() {
            currentPage++
            binding.vpBackgroundSlider.setCurrentItem(currentPage, true)
            sliderHandler.postDelayed(this, 3000) // Change every 3 seconds
        }
    }
    
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
        setupImageSlider()
        setupRecyclerViews()
        setupScrollBehavior()
    }
    
    private fun setupImageSlider() {
        sliderAdapter = ImageSliderAdapter(sliderImages)
        binding.vpBackgroundSlider.adapter = sliderAdapter
        
        // Disable preloading adjacent pages to show only one image at a time
        binding.vpBackgroundSlider.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        
        // Add page transformer to ensure only current page is fully visible
        binding.vpBackgroundSlider.setPageTransformer { page, position ->
            page.alpha = if (position == 0f) 1f else 0f
        }
        
        // Start from middle to allow scrolling in both directions
        val startPosition = 500 // Middle of 1000 items
        binding.vpBackgroundSlider.setCurrentItem(startPosition, false)
        currentPage = startPosition
        
        // Setup segmented line indicators
        setupLineIndicators()
        updateLineIndicators(startPosition % sliderImages.size)
        
        // Add page change callback to update progress
        binding.vpBackgroundSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val actualPosition = position % sliderImages.size
                updateLineIndicators(actualPosition)
            }
        })
        
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
    
    private fun setupLineIndicators() {
        binding.llLineIndicators.removeAllViews()
        for (i in sliderImages.indices) {
            val line = View(requireContext()).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    (25 * resources.displayMetrics.density).toInt(),  // 25dp width (horizontal)
                    (4 * resources.displayMetrics.density).toInt()    // 4dp height
                ).apply {
                    setMargins(
                        (4 * resources.displayMetrics.density).toInt(),  // 4dp gap on left
                        0,
                        (4 * resources.displayMetrics.density).toInt(),  // 4dp gap on right
                        0
                    )
                }
                background = requireContext().getDrawable(R.drawable.indicator_active)
            }
            binding.llLineIndicators.addView(line)
        }
    }
    
    private fun updateLineIndicators(position: Int) {
        for (i in 0 until binding.llLineIndicators.childCount) {
            val line = binding.llLineIndicators.getChildAt(i)
            if (i == position) {
                // Active line - longer and brighter
                line.background = requireContext().getDrawable(R.drawable.indicator_active)
                line.animate()
                    .scaleX(1.3f)
                    .setDuration(200)
                    .start()
            } else {
                // Inactive line - normal size
                line.background = requireContext().getDrawable(R.drawable.indicator_inactive)
                line.animate()
                    .scaleX(1.0f)
                    .setDuration(200)
                    .start()
            }
        }
    }
    
    private fun setupRecyclerViews() {
        // Latest Releases
        latestReleasesAdapter = ForYouAdapter(contentPosters) { position ->
            Toast.makeText(requireContext(), "Latest: ${position}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvLatestReleases.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = latestReleasesAdapter
        }
        
        // Trending Now
        trendingAdapter = ForYouAdapter(contentPosters) { position ->
            Toast.makeText(requireContext(), "Trending: ${position}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }
        
        // Continue Watching
        continueWatchingAdapter = ForYouAdapter(contentPosters) { position ->
            Toast.makeText(requireContext(), "Continue: ${position}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvContinueWatching.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = continueWatchingAdapter
        }
    }
    
    private fun setupScrollBehavior() {
        binding.scrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            // Calculate opacity for background based on scroll
            val maxScroll = 400 * resources.displayMetrics.density  // 400dp spacer height
            val alpha = (scrollY / maxScroll).coerceIn(0f, 1f)
            
            // Fade out background as user scrolls
            binding.backgroundContainer.alpha = 1f - (alpha * 0.7f)  // Fade to 30% opacity
            
            // Scale down background slightly for parallax effect
            val scale = 1f - (alpha * 0.1f)  // Scale down to 90%
            binding.vpBackgroundSlider.scaleX = scale
            binding.vpBackgroundSlider.scaleY = scale
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Restart auto-slide when fragment becomes visible
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
    
    override fun onPause() {
        super.onPause()
        // Stop auto-slide when fragment is not visible
        sliderHandler.removeCallbacks(sliderRunnable)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }
}
