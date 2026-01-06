package com.livestreaming.tv.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "live_streaming_tv")

@Module
@InstallIn(SingletonComponent::class)
class AppPrefModule {

    @Provides
    @Singleton
    fun provideDataSote(@ApplicationContext context: Context): DataStore<Preferences> =
        context.datastore
}