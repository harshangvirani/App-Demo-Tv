package com.livestreaming.tv.player

import android.content.Context
import android.net.Uri
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

class VLCPlayerWrapper(context: Context) : IMediaPlayer {
    
    private val libVLC: LibVLC = LibVLC(context)
    private val mediaPlayer: MediaPlayer = MediaPlayer(libVLC)
    
    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying
        
    override val currentPosition: Long
        get() = mediaPlayer.time
        
    override val duration: Long
        get() = mediaPlayer.length
    
    override fun play() {
        mediaPlayer.play()
    }
    
    override fun pause() {
        mediaPlayer.pause()
    }
    
    override fun seekTo(positionMs: Long) {
        mediaPlayer.time = positionMs
    }
    
    override fun setPlaybackSpeed(speed: Float) {
        mediaPlayer.rate = speed
    }
    
    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
        libVLC.release()
    }
    
    override fun setVideoSurface(surface: android.view.Surface?) {
        mediaPlayer.vlcVout.apply {
            detachViews()
            if (surface != null) {
                setVideoSurface(surface, null)
                attachViews()
            }
        }
    }
    
    fun setMediaUri(uri: Uri) {
        val media = Media(libVLC, uri)
        mediaPlayer.media = media
        media.release()
    }
    
    fun getMediaPlayer(): MediaPlayer = mediaPlayer
}
