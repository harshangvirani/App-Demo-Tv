package com.livestreaming.tv.player

interface IMediaPlayer {
    val isPlaying: Boolean
    val currentPosition: Long
    val duration: Long
    
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setPlaybackSpeed(speed: Float)
    fun release()
    
    fun setVideoSurface(surface: android.view.Surface?)
}
