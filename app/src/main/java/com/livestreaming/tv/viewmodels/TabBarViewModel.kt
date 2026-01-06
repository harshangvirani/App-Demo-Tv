package com.livestreaming.tv.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabBarViewModel @Inject constructor() : ViewModel() {

    private val _scrollToTop = MutableLiveData<Unit>()
    val scrollToTop: LiveData<Unit> = _scrollToTop

    fun requestScrollToTop() {
        _scrollToTop.value = Unit
    }
}
