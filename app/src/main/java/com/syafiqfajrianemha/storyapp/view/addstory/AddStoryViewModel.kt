package com.syafiqfajrianemha.storyapp.view.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.syafiqfajrianemha.storyapp.data.local.datastore.model.UserModel
import com.syafiqfajrianemha.storyapp.data.remote.response.FileUploadResponse
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _fileUploadResponse = MutableLiveData<FileUploadResponse>()
    val fileUploadResponse: LiveData<FileUploadResponse> = _fileUploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addStory(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response =
                    repository.addStory("Bearer $token", multipartBody, description, lat, lon)

                _fileUploadResponse.postValue(response)
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, FileUploadResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _fileUploadResponse.postValue(errorBody)

                Log.d(TAG, "Upload File Error: $errorMessage")
            }
        }
    }

    fun getUser(): LiveData<UserModel> = repository.getUser()

    companion object {
        private val TAG = AddStoryViewModel::class.java.simpleName
    }
}