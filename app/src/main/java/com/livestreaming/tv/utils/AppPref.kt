package com.livestreaming.tv.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPref @Inject constructor(private val dataStore: DataStore<Preferences>) {

    //get user is Login
    val isLogin: Flow<Boolean> = dataStore.data.map {
        it[IS_LOGIN] ?: false
    }

    //set is Login
    suspend fun setIsLogin(isLogin: Boolean) {
        dataStore.edit {
            it[IS_LOGIN] = isLogin
        }
    }


    //get Language
    val selectedLanguage: Flow<String> = dataStore.data.map {
        it[SELECTED_LANGUAGE] ?: ""
    }

    //setLanguage
    suspend fun setSelectedLanguage(language: String) {
        dataStore.edit {
            it[SELECTED_LANGUAGE] = language
        }
    }

    //Get Subtitle
    val getSubtitle: Flow<Boolean> = dataStore.data.map {
        it[SUBTITLE_SETTINGS] ?: false
    }

    //Set Subtitle settings
    suspend fun setSubtitleSettings(isOn: Boolean) {
        dataStore.edit {
            it[SUBTITLE_SETTINGS] = isOn
        }
    }

    //Get Playback Speed
    val getPlaybackSpeed: Flow<String> = dataStore.data.map {
        it[PLAYBACK_SPEED_SETTINGS] ?: ""
    }

    //Set Playback Speed
    suspend fun setPlaybackSpeed(playbackSpeed: String) {
        dataStore.edit {
            it[PLAYBACK_SPEED_SETTINGS] = playbackSpeed
        }
    }

    //Get Quality
    val getQuality: Flow<String> = dataStore.data.map {
        it[QUALITY_SETTINGS] ?: ""
    }

    //Set Quality
    suspend fun setQuality(quality: String) {
        dataStore.edit {
            it[QUALITY_SETTINGS] = quality
        }
    }

    companion object PrefKeys {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val SUBTITLE_SETTINGS = booleanPreferencesKey("is_on")
        val PLAYBACK_SPEED_SETTINGS = stringPreferencesKey("playback_speed")
        val QUALITY_SETTINGS = stringPreferencesKey("quality")
    }
}