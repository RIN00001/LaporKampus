package com.example.laporkampus.screens.uistates

import com.example.laporkampus.datas.models.UserResponse

sealed interface AuthenticationUiStatus {
    object Start: AuthenticationUiStatus
    object Loading: AuthenticationUiStatus
    data class Success(val userData: UserResponse): AuthenticationUiStatus
    data class Error(val message: String): AuthenticationUiStatus
}