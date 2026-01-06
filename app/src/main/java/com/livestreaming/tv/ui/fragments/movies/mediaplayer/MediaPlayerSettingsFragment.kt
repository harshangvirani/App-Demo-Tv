package com.livestreaming.tv.ui.fragments.movies.mediaplayer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.textview.MaterialTextView
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.FragmentMediaPlayerSettingsBinding
import com.livestreaming.tv.utils.MediaPlayerConstants.QUALITY_1080P
import com.livestreaming.tv.utils.MediaPlayerConstants.QUALITY_720P
import com.livestreaming.tv.utils.MediaPlayerConstants.QUALITY_AUTO
import com.livestreaming.tv.utils.MediaPlayerConstants.QUALITY_DATA
import com.livestreaming.tv.utils.MediaPlayerConstants.SPEED_0_5
import com.livestreaming.tv.utils.MediaPlayerConstants.SPEED_1_0
import com.livestreaming.tv.utils.MediaPlayerConstants.SPEED_1_25
import com.livestreaming.tv.utils.MediaPlayerConstants.SPEED_1_5
import com.livestreaming.tv.utils.getFocusHighlightListener
import com.livestreaming.tv.viewmodels.MediaPlayerSettingsViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaPlayerSettingsFragment : DialogFragment() {

    private var _binding: FragmentMediaPlayerSettingsBinding? = null
    private val binding get() = _binding!!

    private val mediaPlayerSettingsViewModels: MediaPlayerSettingsViewModels by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMediaPlayerSettingsBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Full height and match parent width
        dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels, // full width
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Align dialog to the right
        dialog?.window?.setGravity(Gravity.END)

        binding.btnBack.setOnClickListener {
            //dismiss()
        }

        mediaPlayerSettingsViewModels.getSubtitle()
        mediaPlayerSettingsViewModels.getPlaybackSpeed()
        mediaPlayerSettingsViewModels.getQuality()
        applySavedSettings()
        setupSubtitle()
        playbackSpeed()
        setOuality()

    }

    private fun applySavedSettings() {
        with(binding) {
            //Apply On Subtitle
            mediaPlayerSettingsViewModels.isSubtitleOn.observe(viewLifecycleOwner) {
                if (it) {
                    selectOption(tvSubtitleOn, listOf(tvSubtitleOff))
                } else {
                    selectOption(tvSubtitleOff, listOf(tvSubtitleOn))
                }
            }

            //Apply On Playback Speed
            mediaPlayerSettingsViewModels.playbackSpeed.observe(viewLifecycleOwner) {
                when (it) {
                    SPEED_0_5 -> {
                        selectOption(
                            tvPlaybackSpeed05,
                            listOf(tvPlaybackSpeed10, tvPlaybackSpeed125, tvPlaybackSpeed15)
                        )
                    }

                    SPEED_1_25 -> {
                        selectOption(
                            tvPlaybackSpeed125,
                            listOf(tvPlaybackSpeed05, tvPlaybackSpeed10, tvPlaybackSpeed15)
                        )
                    }

                    SPEED_1_5 -> {
                        selectOption(
                            tvPlaybackSpeed15,
                            listOf(tvPlaybackSpeed05, tvPlaybackSpeed10, tvPlaybackSpeed125)
                        )
                    }

                    else -> {
                        selectOption(
                            tvPlaybackSpeed10,
                            listOf(tvPlaybackSpeed05, tvPlaybackSpeed125, tvPlaybackSpeed15)
                        )
                    }
                }
            }

            //Apply On Quality
            mediaPlayerSettingsViewModels.quality.observe(viewLifecycleOwner) {
                when (it) {
                    QUALITY_AUTO -> selectOption(
                        tvAuto,
                        listOf(tvFullHd1080, tvHd720, tvDataSave)
                    )

                    QUALITY_720P -> selectOption(
                        tvHd720,
                        listOf(tvAuto, tvFullHd1080, tvDataSave)
                    )

                    QUALITY_DATA -> selectOption(
                        tvDataSave,
                        listOf(tvAuto, tvFullHd1080, tvHd720)
                    )

                    else -> selectOption(
                        tvFullHd1080,
                        listOf(tvAuto, tvHd720, tvDataSave)
                    )
                }
            }
        }
    }

    private fun setupSubtitle() {
        with(binding) {

            tvSubtitleOn.setOnClickListener {
                mediaPlayerSettingsViewModels.setSubtitle(true)
                selectOption(tvSubtitleOn, listOf(tvSubtitleOff))
            }

            tvSubtitleOff.setOnClickListener {
                mediaPlayerSettingsViewModels.setSubtitle(false)
                selectOption(tvSubtitleOff, listOf(tvSubtitleOn))
            }

            // Focus change listeners
            tvSubtitleOn.onFocusChangeListener = getFocusHighlightListener()
            tvSubtitleOff.onFocusChangeListener = getFocusHighlightListener()

        }
    }

    private fun playbackSpeed() {
        with(binding) {

            tvPlaybackSpeed05.setOnClickListener {
                mediaPlayerSettingsViewModels.setPlaybackSpeed(SPEED_0_5)
                selectOption(
                    tvPlaybackSpeed05,
                    listOf(tvPlaybackSpeed10, tvPlaybackSpeed125, tvPlaybackSpeed15)
                )
            }
            tvPlaybackSpeed10.setOnClickListener {
                mediaPlayerSettingsViewModels.setPlaybackSpeed(SPEED_1_0)
                selectOption(
                    tvPlaybackSpeed10,
                    listOf(tvPlaybackSpeed05, tvPlaybackSpeed125, tvPlaybackSpeed15)
                )
            }
            tvPlaybackSpeed125.setOnClickListener {
                mediaPlayerSettingsViewModels.setPlaybackSpeed(SPEED_1_25)
                selectOption(
                    tvPlaybackSpeed125,
                    listOf(tvPlaybackSpeed10, tvPlaybackSpeed05, tvPlaybackSpeed15)
                )
            }
            tvPlaybackSpeed15.setOnClickListener {
                mediaPlayerSettingsViewModels.setPlaybackSpeed(SPEED_1_5)
                selectOption(
                    tvPlaybackSpeed15,
                    listOf(tvPlaybackSpeed10, tvPlaybackSpeed05, tvPlaybackSpeed125)
                )
            }

            // Focus change listeners
            listOf(
                tvPlaybackSpeed05,
                tvPlaybackSpeed10,
                tvPlaybackSpeed125,
                tvPlaybackSpeed15
            ).forEach {
                it.onFocusChangeListener = getFocusHighlightListener()
            }
        }
    }

    private fun setOuality() {
        with(binding) {
            tvAuto.setOnClickListener {
                mediaPlayerSettingsViewModels.setQuality(QUALITY_AUTO)
                selectOption(
                    tvAuto,
                    listOf(tvFullHd1080, tvHd720, tvDataSave)
                )
            }
            tvFullHd1080.setOnClickListener {
                mediaPlayerSettingsViewModels.setQuality(QUALITY_1080P)
                selectOption(
                    tvFullHd1080,
                    listOf(tvAuto, tvHd720, tvDataSave)
                )
            }
            tvHd720.setOnClickListener {
                mediaPlayerSettingsViewModels.setQuality(QUALITY_720P)
                selectOption(tvHd720, listOf(tvAuto, tvFullHd1080, tvDataSave))
            }
            tvDataSave.setOnClickListener {
                mediaPlayerSettingsViewModels.setQuality(QUALITY_DATA)
                selectOption(tvDataSave, listOf(tvAuto, tvFullHd1080, tvHd720))
            }

            listOf(
                tvAuto, tvFullHd1080, tvHd720, tvDataSave
            ).forEach {
                it.onFocusChangeListener = getFocusHighlightListener()
            }
        }
    }

    fun selectOption(selected: MaterialTextView, unselected: List<MaterialTextView>) {
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        unselected.forEach {
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
    }

    companion object{
        const val TAG = "MediaPlayerSettingsDialogTags"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
