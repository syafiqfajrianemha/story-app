package com.syafiqfajrianemha.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syafiqfajrianemha.storyapp.data.repository.StoryRepository
import com.syafiqfajrianemha.storyapp.di.Injection
import com.syafiqfajrianemha.storyapp.view.addstory.AddStoryViewModel
import com.syafiqfajrianemha.storyapp.view.login.LoginViewModel
import com.syafiqfajrianemha.storyapp.view.main.MainViewModel
import com.syafiqfajrianemha.storyapp.view.maps.MapsViewModel
import com.syafiqfajrianemha.storyapp.view.signup.SignupViewModel

class ViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(this.repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}