package com.syafiqfajrianemha.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.syafiqfajrianemha.storyapp.data.local.datastore.model.UserModel
import com.syafiqfajrianemha.storyapp.data.remote.response.StoryResponse
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storiesWithLocationResponse = MutableLiveData<StoryResponse>()
    val storiesWithLocationResponse: LiveData<StoryResponse> = _storiesWithLocationResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoriesWithLocation(token: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation("Bearer $token")

                _isLoading.postValue(false)
                _storiesWithLocationResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun getUser(): LiveData<UserModel> = repository.getUser()

    companion object {
        private val TAG = MapsViewModel::class.java.simpleName
    }
}