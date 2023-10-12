package com.syafiqfajrianemha.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.syafiqfajrianemha.storyapp.data.local.datastore.UserPreferences
import com.syafiqfajrianemha.storyapp.data.remote.retrofit.ApiConfig
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, pref)
    }
}