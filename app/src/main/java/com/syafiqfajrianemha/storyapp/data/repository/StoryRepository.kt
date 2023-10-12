package com.syafiqfajrianemha.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.syafiqfajrianemha.storyapp.data.StoryPagingSource
import com.syafiqfajrianemha.storyapp.data.local.datastore.UserPreferences
import com.syafiqfajrianemha.storyapp.data.local.datastore.model.UserModel
import com.syafiqfajrianemha.storyapp.data.remote.response.ListStoryItem
import com.syafiqfajrianemha.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun signup(name: String, email: String, password: String) =
        apiService.signup(name, email, password)

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(token: String) = apiService.getStoriesWithLocation(token)

    suspend fun addStory(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) = apiService.uploadImage(token, multipartBody, description, lat, lon)

    fun getUser() = userPreferences.getUser().asLiveData()

    fun getToken() = runBlocking { userPreferences.getUser().first().token }

    suspend fun saveUser(user: UserModel) = userPreferences.saveUser(user)

    suspend fun logout() = userPreferences.logout()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}