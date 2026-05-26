package com.example.laporkampus.datas.services
import com.example.laporkampus.datas.models.LoginRequest
import com.example.laporkampus.datas.models.LoginResponse
import com.example.laporkampus.datas.models.RegisterRequest
import com.example.laporkampus.datas.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

interface AuthenticationService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}