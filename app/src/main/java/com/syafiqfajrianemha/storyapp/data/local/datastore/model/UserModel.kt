package com.syafiqfajrianemha.storyapp.data.local.datastore.model

data class UserModel(
    val userId: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean
)