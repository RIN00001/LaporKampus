package com.example.laporkampus.datas.repositories

import com.example.laporkampus.datas.models.UserResponse
import com.example.laporkampus.datas.services.AuthenticationService
import retrofit2.Call

interface AuthenticationRepositoryInterface {
    fun register(name: String, email: String, password: String): Call<UserResponse>
    fun login(email: String, password: String): Call<UserResponse>
}
class AuthenticationRepository(
    private val authAPIService: AuthenticationService
): AuthenticationRepositoryInterface {
    override fun register(name: String, email: String, password: String): Call<UserResponse> {
        var registerMap = HashMap<String, String>()
        registerMap["name"] = name
        registerMap["email"] = email
        registerMap["password"] = password

        return authAPIService.register(registerMap)
    }

    override fun login(email: String, password: String): Call<UserResponse> {
        var loginMap = HashMap<String, String>()

        loginMap["email"] = email
        loginMap["password"] = password

        return authAPIService.login(loginMap)
    }

}