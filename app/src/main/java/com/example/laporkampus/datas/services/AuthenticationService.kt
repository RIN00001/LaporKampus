package com.example.laporkampus.datas.services
import com.example.laporkampus.datas.models.UserResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface AuthenticationService {
    @POST("api/auth/register")
    fun register(@Body registerMap: HashMap<String, String>): Call<UserResponse>

    @POST("api/auth/login")
    fun login(@Body loginMap: HashMap<String, String>): Call<UserResponse>
}