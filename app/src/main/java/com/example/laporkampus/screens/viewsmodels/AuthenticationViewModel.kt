package com.example.laporkampus.screens.viewsmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laporkampus.LaporKampusApplication
import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.UserResponse
import com.example.laporkampus.datas.repositories.AuthenticationRepositoryInterface
import com.example.laporkampus.datas.repositories.UserRepositoryInterface
import com.example.laporkampus.screens.uistates.AuthenticationUiState
import com.example.laporkampus.screens.uistates.AuthenticationUiStatus
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(
    private val authenticationRepository: AuthenticationRepositoryInterface,
    private val userRepository: UserRepositoryInterface
): ViewModel() {
    var authenticationUiStatus: AuthenticationUiStatus by mutableStateOf(AuthenticationUiStatus.Start)
        private set

    private val _authUiState = MutableStateFlow(AuthenticationUiState())

    var usernameInput by mutableStateOf("")
        private set
    var emailInput by mutableStateOf("")
        private set
    var passwordInput by mutableStateOf("")
        private set

    // Input handling
    // Register Input Handling
    fun changeUsernameInput(input: String) {
        usernameInput = input
        checkRegisterForm()
    }

    fun changeEmailInput(input: String) {
        emailInput = input
        checkRegisterForm()
    }

    fun changePasswordInput(input: String) {
        passwordInput = input
        checkRegisterForm()
    }

    // Login Input Handling
    fun changeLoginEmail(input: String) {
        emailInput = input
        checkLoginForm()
    }

    fun changeLoginPassword(input: String) {
        passwordInput = input
        checkLoginForm()
    }

    // UI Logic for login & register
    fun checkLoginForm() {
        // Login cuma butuh Email & Password
        val isValid = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
        _authUiState.update { it.copy(buttonEnabled = isValid) }
    }

    fun checkRegisterForm() {
        // Register butuh Username, Email, Password
        val isValid = emailInput.isNotEmpty() &&
                passwordInput.isNotEmpty() &&
                usernameInput.isNotEmpty()

        _authUiState.update { it.copy(buttonEnabled = isValid) }
    }

    fun resetViewModel() {
        emailInput = ""
        passwordInput = ""
        usernameInput = ""
        authenticationUiStatus = AuthenticationUiStatus.Start
        _authUiState.update { AuthenticationUiState() }
    }

    fun clearErrorMessage() {
        if (authenticationUiStatus is AuthenticationUiStatus.Error) {
            authenticationUiStatus = AuthenticationUiStatus.Start
        }
    }

    // API Calls to backend

    fun login() {
        Log.d("PayloadCheck", "Email: '$emailInput', Password: '$passwordInput'")
        authenticationUiStatus = AuthenticationUiStatus.Loading
        val call = authenticationRepository.login(emailInput,passwordInput)

        call.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful) {
                    val body = response.body()
                    val user = body?.user
                    val token = body?.token

                    if (token!= null && user != null) {
                        saveUserSession(token, user.name)
                        authenticationUiStatus = AuthenticationUiStatus.Success(body)
                        Log.d("Login", "Account accessed: $token")
                    } else {
                        authenticationUiStatus = AuthenticationUiStatus.Error("Login is done but data/token is empty")
                    }
                } else {
                    val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    authenticationUiStatus = AuthenticationUiStatus.Error(errorMsg)
                    Log.e("AuthViewModel", "Login Error: $errorMsg")
                }
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                val msg = t.localizedMessage ?: "Network Error"
                authenticationUiStatus = AuthenticationUiStatus.Error(msg)
                Log.e("Authentication Failure", "Login Failure: $msg")
            }
        })
    }

    fun register() {
        authenticationUiStatus = AuthenticationUiStatus.Loading
        val call = authenticationRepository.register(usernameInput, emailInput, passwordInput)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        authenticationUiStatus = AuthenticationUiStatus.Success(body)
                        Log.d("AuthViewModel", "Register Success ${body}")
                    } else {
                        authenticationUiStatus = AuthenticationUiStatus.Error("Register berhasil tapi respon kosong")
                    }
                } else {
                    val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    authenticationUiStatus = AuthenticationUiStatus.Error(errorMsg)
                    Log.e("AuthViewModel", "Register Error: $errorMsg")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                val msg = t.localizedMessage ?: "Network Error"
                authenticationUiStatus = AuthenticationUiStatus.Error(msg)
                Log.e("AuthViewModel", "Register Failure: $msg")
            }
        })
    }

    // Helper
    private fun saveUserSession(token: String, username: String) {
        viewModelScope.launch {
            userRepository.saveUserToken(token)
            userRepository.saveUsername(username)
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody == null) return "Unknown Error"
            val errorResponse = Gson().fromJson(errorBody, GeneralResponseModel::class.java)
            errorResponse.message
        } catch (e: Exception) {
            "Gagal memproses error server"
        }
    }

    // Factory
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LaporKampusApplication)
                AuthenticationViewModel(
                    authenticationRepository = application.container.authRepository,
                    userRepository = application.container.userRepository
                )
            }
        }
    }
}