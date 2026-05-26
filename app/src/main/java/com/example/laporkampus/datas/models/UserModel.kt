package com.example.laporkampus.datas.models

data class UserModel (
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val division: String?
)