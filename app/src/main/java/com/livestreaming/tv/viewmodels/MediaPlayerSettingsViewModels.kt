package com.livestreaming.tv.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livestreaming.tv.utils.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPlayerSettingsViewModels @Inject constructor(private val appPref: AppPref) :
    ViewModel() {

    private var _isSubtitleOn: MutableLiveData<Boolean> = MutableLiveData()
    val isSubtitleOn: LiveData<Boolean> get() = _isSubtitleOn

    private var _playbackSpeed: MutableLiveData<String> = MutableLiveData()
    val playbackSpeed: LiveData<String> get() = _playbackSpeed

    private var _quality: MutableLiveData<String> = MutableLiveData()
    val quality: LiveData<String> get() = _quality


    //Subtitle
    fun setSubtitle(isOn: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        appPref.setSubtitleSettings(isOn)
        getSubtitle()
    }

    fun getSubtitle() = viewModelScope.launch(Dispatchers.IO) {
        appPref.getSubtitle.collect {
            _isSubtitleOn.postValue(it)
        }
    }

    //Playback Speed
    fun setPlaybackSpeed(playbackSpeed: String) = viewModelScope.launch(Dispatchers.IO) {
        appPref.setPlaybackSpeed(playbackSpeed)
        getPlaybackSpeed()
    }

    fun getPlaybackSpeed() = viewModelScope.launch(Dispatchers.IO) {
        appPref.getPlaybackSpeed.collect {
            _playbackSpeed.postValue(it)
        }
    }

    //Quality
    fun setQuality(quality: String) = viewModelScope.launch(Dispatchers.IO) {
        appPref.setQuality(quality)
        getQuality()
    }

    fun getQuality() = viewModelScope.launch(Dispatchers.IO) {
        appPref.getQuality.collect {
            _quality.postValue(it)
        }
    }

}