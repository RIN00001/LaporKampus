package com.example.laporkampus.datas.models

data class LoginResponse (
    val message: String,
    val token: String,
    val user: UserModel
)