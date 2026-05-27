package com.example.laporkampus.screens.uistates

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class AuthenticationUiState (
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val passwordVisibility: VisualTransformation = PasswordVisualTransformation(),
    val confirmPasswordVisibility: VisualTransformation = PasswordVisualTransformation(),
//    val passwordVisibilityIcon: Int = R.drawable.ic_password_visible,
//    val confirmPasswordVisibilityIcon: Int = R.drawable.ic_password_visible,
    val buttonEnabled: Boolean = false
)