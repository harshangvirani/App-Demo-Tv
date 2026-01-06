package com.livestreaming.tv.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livestreaming.tv.utils.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val appPref: AppPref
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow("de") // Default German (3rd item)
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun selectLanguage(languageCode: String) {
        viewModelScope.launch {
            _selectedLanguage.value = languageCode
            appPref.setSelectedLanguage(languageCode)
        }
    }

    fun getDefaultLanguageIndex(): Int {
        // Return index for default language, 3rd item = index 2
        return when (_selectedLanguage.value) {
            "en" -> 0
            "es" -> 1
            "de" -> 2
            "fr" -> 3
            "hi" -> 4
            "ar" -> 5
            "zh" -> 6
            "ja" -> 7
            else -> 2 // Default to German
        }
    }
}