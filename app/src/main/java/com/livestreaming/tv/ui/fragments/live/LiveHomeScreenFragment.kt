package com.livestreaming.tv.ui.fragments.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.MoviesRelatedAdapter
import com.livestreaming.tv.adapter.StarCastAdapter
import com.livestreaming.tv.databinding.FragmentLiveHomeScreenBinding
import com.livestreaming.tv.ui.fragments.movies.MoviesHomeScreenFragmentDirections
import com.livestreaming.tv.utils.contentPosters
import com.livestreaming.tv.utils.starCast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveHomeScreenFragment : Fragment() {

    private var _binding: FragmentLiveHomeScreenBinding? = null
    private val binding get() = _binding!!

    private var starCastAdapter: StarCastAdapter? = null
    private var moviesRelatedAdapter: MoviesRelatedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLiveHomeScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundImage()
        setupStarCast()
        setRecyclerView()
    }

    private fun setBackgroundImage() {
        with(binding) {
            spBgImg.setImageResource(R.drawable.slid_1)
            tvHeroTitle.text = getString(R.string.the_last_of_us)
            tvHeroMetadata.text = getString(R.string.the_last_of_us)
            tvHeroGenres.text = getString(R.string.the_last_of_us)
            tvHeroDescription.text = getString(R.string.the_last_of_us)
            // binding.btnWatchNow.visibleIf(!item.showButtons)
            binding.btnWatchNow.setOnClickListener {
                findNavController().navigate(MoviesHomeScreenFragmentDirections.actionMoviesHomeScreenFragmentToMediaPlayerFragment())
            }
        }
    }

    private fun setupStarCast() {
        starCastAdapter = StarCastAdapter()
        starCastAdapter?.starCastList?.submitList(starCast)
        with(binding) {
            rvStarCast.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = starCastAdapter
            }

            btnAllStarCast.setOnClickListener {
                starCastAdapter?.toggleExpand()
                btnAllStarCast.text =
                    if (starCastAdapter?.isExpanded == true) getString(R.string.less) else getString(
                        R.string.all_cast
                    )
            }
        }
    }

    private fun setRecyclerView() {
        moviesRelatedAdapter = MoviesRelatedAdapter()
        moviesRelatedAdapter?.imageList?.submitList(contentPosters)
        with(binding) {
            rvRelatedMovie.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = moviesRelatedAdapter
            }
        }
        moviesRelatedAdapter?.onClick = {
        }
    }

}