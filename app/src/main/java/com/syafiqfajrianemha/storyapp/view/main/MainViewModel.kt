package com.syafiqfajrianemha.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.syafiqfajrianemha.storyapp.data.local.datastore.model.UserModel
import com.syafiqfajrianemha.storyapp.data.remote.response.ListStoryItem
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    //    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
//        return repository.getStories("Bearer $token ").cachedIn(viewModelScope)
//    }
    val stories: LiveData<PagingData<ListStoryItem>> by lazy {
        repository.getStories("Bearer ${repository.getToken()}").cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<UserModel> = repository.getUser()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}