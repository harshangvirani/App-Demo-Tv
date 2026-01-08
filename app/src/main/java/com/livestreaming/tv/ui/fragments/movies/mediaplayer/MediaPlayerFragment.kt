package com.livestreaming.tv.ui.fragments.movies.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.TimeBar
import androidx.navigation.fragment.navArgs
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.MovieShowImageAdapter
import com.livestreaming.tv.databinding.FragmentMediaPlayerBinding
import com.livestreaming.tv.utils.IS_LIVE
import com.livestreaming.tv.utils.formatTime
import com.livestreaming.tv.utils.goneIf
import com.livestreaming.tv.utils.log
import com.livestreaming.tv.utils.toast
import com.livestreaming.tv.viewmodels.MediaPlayerSettingsViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaPlayerFragment : Fragment() {

    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!

    // Player management
    private lateinit var exoPlayerWrapper: com.livestreaming.tv.player.ExoPlayerWrapper
    private lateinit var vlcPlayerWrapper: com.livestreaming.tv.player.VLCPlayerWrapper
    private var currentPlayer: com.livestreaming.tv.player.IMediaPlayer? = null
    private var isUsingVLC = false
    
    private var movieShowImageAdapter: MovieShowImageAdapter? = null
    private var isLocked = false
    private var aspectFit = true
    private var isFullScreen = false
    private var isUserScrubbing = false
    private var isOverlayVisible = false
    private val mediaPlayerSettingsViewModels: MediaPlayerSettingsViewModels by viewModels()

    private val args: MediaPlayerFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMediaPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePlayers()
        setupCustomControls()
        setupRecyclerView()
        setupOverlayRecyclerView()
        makeControllerButtonsFocusable()
        setSettings()
        setObserver()

        binding.btnSettings.requestFocus()
        binding.btnSettings.isFocusable = true
        binding.btnSettings.isFocusableInTouchMode = true
        // Global key listener for remote
        binding.root.isFocusableInTouchMode = true
        binding.root.post {
            binding.root.requestFocus()
        }

        binding.root.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val focusedView = binding.root.findFocus()
                
                // Handle D-pad UP to show/hide overlay RecyclerView (toggle)
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (!isOverlayVisible && focusedView != binding.rvOverlay) {
                        // Show overlay if hidden
                        showOverlay()
                        return@setOnKeyListener true
                    } else if (isOverlayVisible && focusedView != binding.rvOverlay) {
                        // Hide overlay if visible and focus is not on it
                        hideOverlay()
                        return@setOnKeyListener true
                    }
                }
                
                // Handle D-pad DOWN to hide overlay RecyclerView
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (isOverlayVisible && focusedView == binding.rvOverlay) {
                        // Check if at the start of RecyclerView
                        val layoutManager = binding.rvOverlay.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager
                        val firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
                        if (firstVisiblePosition == 0) {
                            hideOverlay()
                            return@setOnKeyListener true
                        }
                    }
                }
                
                // Show controller globally
                if (!binding.mdMediaPlayerView.isControllerFullyVisible) {
                    binding.mdMediaPlayerView.showController()
                    // Post delay ensures controller layout is ready
                    binding.root.post {
                        binding.btnSettings.requestFocus()
                    }
                }

                // OK / Enter button toggles play/pause (except on control buttons)
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Allow control buttons to handle their own clicks
                    val controlButtonIds = listOf(
                        R.id.btn_settings,
                        R.id.btn_lock,
                        R.id.btn_screen_ration,
                        R.id.btn_audio_subtitle,
                        R.id.btn_full_screen,
                        R.id.exo_play_pose,
                        R.id.exo_play_back,
                        R.id.exo_play_next
                    )
                    
                    if (focusedView?.id in controlButtonIds) {
                        return@setOnKeyListener false // Let the button handle the click
                    }
                    
                    // Toggle play/pause for player area
                    currentPlayer?.let {
                        if (it.isPlaying) it.pause() else it.play()
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayers() {
        // Initialize ExoPlayer
        exoPlayerWrapper = com.livestreaming.tv.player.ExoPlayerWrapper(requireContext())
        binding.mdMediaPlayerView.player = exoPlayerWrapper.getPlayer()
        
        // Initialize VLC (disabled for now to prevent auto-switching)
        // vlcPlayerWrapper = com.livestreaming.tv.player.VLCPlayerWrapper(requireContext())
        
        // Set default player (ExoPlayer only)
        currentPlayer = exoPlayerWrapper
        isUsingVLC = false
        
        // Setup media
        val mediaUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        exoPlayerWrapper.setMediaItem(mediaUri)
        // vlcPlayerWrapper.setMediaUri(mediaUri)
        
        // Apply saved subtitle state
        val trackSelector = exoPlayerWrapper.getPlayer().trackSelector as DefaultTrackSelector
        val subtitleOn = mediaPlayerSettingsViewModels.isSubtitleOn.value ?: true
        val parameters = trackSelector.parameters
            .buildUpon()
            .setRendererDisabled(C.TRACK_TYPE_TEXT, !subtitleOn)
            .build()
        trackSelector.setParameters(parameters)
        
        // Controller behavior
        binding.mdMediaPlayerView.controllerAutoShow = true
        binding.mdMediaPlayerView.controllerShowTimeoutMs = 3000
        
        // Configure for live vs on-demand content
        if (args.MediaType == IS_LIVE) {
            // For live streams: configure buffering display
            // ExoPlayer automatically disables seeking for live content
            binding.mdMediaPlayerView.setShowBuffering(androidx.media3.ui.PlayerView.SHOW_BUFFERING_ALWAYS)
            log("Configured for LIVE stream playback - seeking automatically disabled by ExoPlayer")
        } else {
            // For on-demand content: show normal buffering
            binding.mdMediaPlayerView.setShowBuffering(androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            log("Configured for ON-DEMAND playback - seeking enabled")
        }
        
        // Start playback
        currentPlayer?.play()
    }
    
    private fun switchPlayer() {
        // Save current state
        val currentPosition = currentPlayer?.currentPosition ?: 0L
        val wasPlaying = currentPlayer?.isPlaying ?: false
        
        // Pause current player
        currentPlayer?.pause()
        
        if (isUsingVLC) {
            // Switch to ExoPlayer
            binding.vlcSurface.visibility = View.GONE
            binding.mdMediaPlayerView.visibility = View.VISIBLE
            currentPlayer = exoPlayerWrapper
            isUsingVLC = false

            // Restore state
            exoPlayerWrapper.seekTo(currentPosition)
            if (wasPlaying) exoPlayerWrapper.play()
        } else {
            // Switch to VLC
            binding.mdMediaPlayerView.visibility = View.GONE
            binding.vlcSurface.visibility = View.VISIBLE
            vlcPlayerWrapper.setVideoSurface(binding.vlcSurface.holder.surface)
            currentPlayer = vlcPlayerWrapper
            isUsingVLC = true

            // Restore state
            vlcPlayerWrapper.seekTo(currentPosition)
            if (wasPlaying) vlcPlayerWrapper.play()
        }
        
        toast("Switched to ${if (isUsingVLC) "VLC" else "ExoPlayer"}")
        
        // Reapply settings
        applyCurrentSettings()
    }
    
    private fun applyCurrentSettings() {
        // Reapply speed
        mediaPlayerSettingsViewModels.playbackSpeed.value?.let { speedString ->
            if (speedString.isNotEmpty()) {
                val speed = speedString.toFloatOrNull() ?: 1.0f
                currentPlayer?.setPlaybackSpeed(speed)
            }
        }
        
        // Reapply quality (ExoPlayer only for now)
        if (!isUsingVLC) {
            mediaPlayerSettingsViewModels.quality.value?.let { quality ->
                if (quality.isNotEmpty()) {
                    applyVideoQuality(quality)
                }
            }
        }
    }
    @OptIn(UnstableApi::class)
    private fun setupCustomControls() {
        val playPauseButton =
            binding.mdMediaPlayerView.findViewById<ShapeableImageView>(R.id.exo_play_pose)
        val progressSlider =
            binding.mdMediaPlayerView.findViewById<DefaultTimeBar>(R.id.exo_progress)
        val totalPlayTimeText =
            binding.mdMediaPlayerView.findViewById<MaterialTextView>(R.id.tv_total_time)
        val currentprogressSliderPlayTimeText =
            binding.mdMediaPlayerView.findViewById<MaterialTextView>(R.id.tv_tracking_time)
        val lockScreenBtn = binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_lock)
        val screenRatioBtn =
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_screen_ration)
        val audioSubtitleBtn =
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_audio_subtitle)
        val fullScreenBtn =
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_full_screen)
        val movieNameTextView =
            binding.mdMediaPlayerView.findViewById<MaterialTextView>(R.id.tv_movie_name)

        val backWord10Secon =
            binding.mdMediaPlayerView.findViewById<ShapeableImageView>(R.id.exo_play_back)
        val nextWord10Secon =
            binding.mdMediaPlayerView.findViewById<ShapeableImageView>(R.id.exo_play_next)

        movieNameTextView.text = "Demo Movies"

        // --- BACK 10 SECONDS ---
        backWord10Secon.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                backWord10Secon.strokeWidth = 2f
                backWord10Secon.strokeColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
            } else {
                backWord10Secon.strokeWidth = 0f
            }
        }
        backWord10Secon.setOnClickListener {
            currentPlayer?.let {
                val newPosition = (it.currentPosition - 10_000L).coerceAtLeast(0L)
                it.seekTo(newPosition)
            }
        }

        // --- NEXT 10 SECONDS ---
        nextWord10Secon.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                nextWord10Secon.strokeWidth = 2f
                nextWord10Secon.strokeColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
            } else {
                nextWord10Secon.strokeWidth = 0f
            }
        }
        nextWord10Secon.setOnClickListener {
            currentPlayer?.let {
                val newPosition = (it.currentPosition + 10_000L).coerceAtMost(it.duration)
                it.seekTo(newPosition)
            }
        }

        // --- PLAY/PAUSE ---
        playPauseButton.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                playPauseButton.strokeWidth = 2f
                playPauseButton.strokeColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
            } else {
                playPauseButton.strokeWidth = 0f
            }
        }
        playPauseButton.setOnClickListener {
            currentPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    playPauseButton.setImageResource(R.drawable.ic_play)
                } else {
                    it.play()
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                }
            }
        }


        // --- PROGRESS SLIDER ---
        progressSlider.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                isUserScrubbing = true
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                currentprogressSliderPlayTimeText.text = formatTime(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                isUserScrubbing = false
                currentPlayer?.seekTo(position)
            }
        })
        exoPlayerWrapper.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (!isUserScrubbing && !isUsingVLC) {
                    progressSlider.setPosition(player.currentPosition)
                    progressSlider.setDuration(player.duration)
                    currentprogressSliderPlayTimeText.text = formatTime(player.currentPosition)
                    totalPlayTimeText.text = " / ${formatTime(player.duration)}"
                }
            }
        })

        // --- LOCK SCREEN ---
        lockScreenBtn.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                lockScreenBtn.strokeWidth = 2
                lockScreenBtn.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                lockScreenBtn.strokeWidth = 0
            }
        }
        lockScreenBtn.setOnClickListener {
            isLocked = !isLocked
            if (isLocked) {
                lockScreenBtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                hidePlayerControls(true)
                toast("Controls Locked")
            } else {
                lockScreenBtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white_9_percent
                    )
                )
                hidePlayerControls(false)
                toast("Controls Unlocked")
            }
        }

        // --- SCREEN RATIO ---
        screenRatioBtn.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                screenRatioBtn.strokeWidth = 2
                screenRatioBtn.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                screenRatioBtn.strokeWidth = 0
            }
        }
        screenRatioBtn.setOnClickListener {
            aspectFit = !aspectFit
            binding.mdMediaPlayerView.resizeMode =
                if (aspectFit) AspectRatioFrameLayout.RESIZE_MODE_FIT else AspectRatioFrameLayout.RESIZE_MODE_FILL
            toast(if (aspectFit) "Aspect Fit" else "Full Fill")
        }

        // --- AUDIO / SUBTITLE BUTTON ---
        audioSubtitleBtn.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                audioSubtitleBtn.strokeWidth = 2
                audioSubtitleBtn.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                audioSubtitleBtn.strokeWidth = 0
            }
        }
        audioSubtitleBtn.setOnClickListener {
            toast("Audio / Subtitle button clicked")
        }

        // --- FULL SCREEN TOGGLE ---
        fullScreenBtn.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                fullScreenBtn.strokeWidth = 2
                fullScreenBtn.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                fullScreenBtn.strokeWidth = 0
            }
        }
        fullScreenBtn.setOnClickListener {
            isFullScreen = !isFullScreen
            val activity = requireActivity()
            if (isFullScreen) {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                toast("Entered Fullscreen")
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                toast("Exited Fullscreen")
            }
        }
    }

    private fun hidePlayerControls(hide: Boolean) {
        with(binding.mdMediaPlayerView) {
            findViewById<DefaultTimeBar>(R.id.exo_progress) goneIf (hide)
            findViewById<ShapeableImageView>(R.id.exo_play_pose).goneIf(hide)
            findViewById<ShapeableImageView>(R.id.exo_play_back).goneIf(hide)
            findViewById<ShapeableImageView>(R.id.exo_play_next).goneIf(hide)
            findViewById<MaterialCardView>(R.id.btn_screen_ration) goneIf (hide)
            findViewById<MaterialCardView>(R.id.btn_audio_subtitle) goneIf (hide)
            findViewById<MaterialCardView>(R.id.btn_full_screen) goneIf (hide)
        }
    }

    private fun setSettings() {
        binding.btnSettings.setOnClickListener {
            log("Click")
            MediaPlayerSettingsFragment().show(
                childFragmentManager,
                MediaPlayerSettingsFragment.TAG
            )

        }

        binding.btnSettings.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.btnSettings.strokeWidth = 2  // 2px border
                binding.btnSettings.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                binding.btnSettings.strokeWidth = 0
            }
        }

    }

    @OptIn(UnstableApi::class)
    private fun setObserver() {
        // Observe subtitle settings
        mediaPlayerSettingsViewModels.isSubtitleOn.observe(viewLifecycleOwner) { subtitleOn ->
            val trackSelector = exoPlayerWrapper?.getPlayer()?.trackSelector as? DefaultTrackSelector
            trackSelector?.let {
                val parameters = it.parameters
                    .buildUpon()
                    .setRendererDisabled(C.TRACK_TYPE_TEXT, !subtitleOn)
                    .build()
                it.setParameters(parameters)
            }
        }
        
        // Observe playback speed from ViewModel
        mediaPlayerSettingsViewModels.playbackSpeed.observe(viewLifecycleOwner) { speedString ->
            if (speedString.isNotEmpty()) {
                val speed = speedString.toFloatOrNull() ?: 1.0f
                // Apply playback speed to current player
                currentPlayer?.setPlaybackSpeed(speed)
                log("Playback speed set to: $speed")
            }
        }
        
        // Observe video quality from ViewModel
        mediaPlayerSettingsViewModels.quality.observe(viewLifecycleOwner) { quality ->
            if (quality.isNotEmpty()) {
                applyVideoQuality(quality)
                log("Video quality set to: $quality")
            }
        }
        
        // Observe subtitle settings
        mediaPlayerSettingsViewModels.isSubtitleOn.observe(viewLifecycleOwner) { subtitleOn ->
            applySubtitleSettings(subtitleOn)
            log("Subtitles ${if (subtitleOn) "enabled" else "disabled"}")
        }
        
        // Start observing playback speed from DataStore
        mediaPlayerSettingsViewModels.getPlaybackSpeed()
        
        // Start observing quality from DataStore
        mediaPlayerSettingsViewModels.getQuality()
        
        // Start observing subtitle settings from DataStore
        mediaPlayerSettingsViewModels.getSubtitle()
    }
    
    @OptIn(UnstableApi::class)
    private fun applySubtitleSettings(enabled: Boolean) {
        // Only apply to ExoPlayer for now
        if (isUsingVLC) {
            log("Subtitle settings not yet supported for VLC player")
            return
        }
        
        val trackSelector = exoPlayerWrapper.getPlayer().trackSelector as? DefaultTrackSelector ?: return
        
        // Build new parameters with subtitle track enabled/disabled
        val parameters = trackSelector.parameters
            .buildUpon()
            .setRendererDisabled(C.TRACK_TYPE_TEXT, !enabled)
            .build()
        
        trackSelector.setParameters(parameters)
    }
    
    @OptIn(UnstableApi::class)
    private fun applyVideoQuality(quality: String) {
        // Only apply to ExoPlayer for now
        if (isUsingVLC) return
        
        val trackSelector = exoPlayerWrapper.getPlayer().trackSelector as? DefaultTrackSelector ?: return
        
        val maxHeight = when (quality.lowercase()) {
            "auto" -> Int.MAX_VALUE
            "1080p", "1080" -> 1080
            "720p", "720" -> 720
            "480p", "480" -> 480
            "360p", "360" -> 360
            "240p", "240" -> 240
            else -> Int.MAX_VALUE // Default to auto
        }
        
        val parameters = trackSelector.parameters
            .buildUpon()
            .setMaxVideoSize(Int.MAX_VALUE, maxHeight)
            .build()
        
        trackSelector.setParameters(parameters)
    }
    
    private fun setupRecyclerView() {
        movieShowImageAdapter = MovieShowImageAdapter().apply {
            imgList.submitList(com.livestreaming.tv.utils.contentPosters)
        }
        
        binding.rvMovies.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = movieShowImageAdapter
        }
    }
    
    private fun setupOverlayRecyclerView() {
        val overlayAdapter = MovieShowImageAdapter().apply {
            imgList.submitList(com.livestreaming.tv.utils.contentPosters)
        }
        
        binding.rvOverlay.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = overlayAdapter
        }
    }
    
    private fun showOverlay() {
        isOverlayVisible = true
        binding.overlayContainer.animate()
            .translationY(0f)
            .setDuration(300)
            .withEndAction {
                // Focus first item in overlay RecyclerView
                binding.rvOverlay.post {
                    binding.rvOverlay.getChildAt(0)?.requestFocus()
                }
            }
            .start()
    }
    
    private fun hideOverlay() {
        isOverlayVisible = false
        binding.overlayContainer.animate()
            .translationY(300f)
            .setDuration(300)
            .withEndAction {
                binding.btnSettings.requestFocus()
            }
            .start()
    }

    private fun makeControllerButtonsFocusable() {
        val buttons = listOf(
            binding.btnSettings,
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_lock),
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_screen_ration),
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_audio_subtitle),
            binding.mdMediaPlayerView.findViewById<MaterialCardView>(R.id.btn_full_screen)
        )

        buttons.forEach {
            it.isFocusable = true
            it.isFocusableInTouchMode = true
        }
    }

    override fun onStop() {
        super.onStop()
        currentPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        currentPlayer?.play()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayerWrapper?.release()
        vlcPlayerWrapper?.release()
        _binding = null
    }
}
