package com.syafiqfajrianemha.storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.syafiqfajrianemha.storyapp.data.remote.response.SignupResponse
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _signupResponse = MutableLiveData<SignupResponse>()
    val signupResponse: LiveData<SignupResponse> = _signupResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signup(name: String, email: String, password: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.signup(name, email, password)

                _isLoading.postValue(false)
                _signupResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignupResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _signupResponse.postValue(errorBody)

                Log.d(TAG, "Signup Error: $errorMessage")
            }
        }
    }

    companion object {
        private val TAG = SignupViewModel::class.java.simpleName
    }
}