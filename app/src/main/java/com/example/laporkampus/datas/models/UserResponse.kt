package com.example.laporkampus.datas.models

data class UserResponse (
    val message: String,
    val token: String?,
    val user: UserModel
)