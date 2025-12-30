package com.example.livestreamingtv.utils

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
    
    // get user is Login
    val isLogin: Flow<Boolean> = dataStore.data.map {
        it[IS_LOGIN] ?: false
    }
    
    // set is Login
    suspend fun setIsLogin(isLogin: Boolean) {
        dataStore.edit {
            it[IS_LOGIN] = isLogin
        }
    }
    
    // get Language
    val selectedLanguage: Flow<String> = dataStore.data.map {
        it[SELECTED_LANGUAGE] ?: ""
    }
    
    // setLanguage
    suspend fun setSelectedLanguage(language: String) {
        dataStore.edit {
            it[SELECTED_LANGUAGE] = language
        }
    }
    
    companion object PrefKeys {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
    }
}
