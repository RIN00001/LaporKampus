package com.example.laporkampus.screen.uiStates

import com.example.laporkampus.datas.models.UserModel

sealed interface LoginUiState {
    object Idle: LoginUiState
    object Loading: LoginUiState
    data class Success(val token: String, val user: UserModel): LoginUiState
    data class Error(val message: String): LoginUiState
}