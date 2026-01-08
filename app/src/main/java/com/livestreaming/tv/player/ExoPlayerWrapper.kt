package com.livestreaming.tv.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerWrapper(context: Context) : IMediaPlayer {
    
    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    
    override val isPlaying: Boolean
        get() = player.isPlaying
        
    override val currentPosition: Long
        get() = player.currentPosition
        
    override val duration: Long
        get() = player.duration
    
    override fun play() {
        player.play()
    }
    
    override fun pause() {
        player.pause()
    }
    
    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }
    
    override fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }
    
    override fun release() {
        player.release()
    }
    
    override fun setVideoSurface(surface: android.view.Surface?) {
        player.setVideoSurface(surface)
    }
    
    fun getPlayer(): ExoPlayer = player
    
    fun setMediaItem(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }
    
    fun addListener(listener: Player.Listener) {
        player.addListener(listener)
    }
}
