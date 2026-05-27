package com.example.laporkampus.datas.interceptors

import com.example.laporkampus.datas.repositories.UserRepositoryInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val userRepo: UserRepositoryInterface): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = try {
            runBlocking {
                userRepo.currentUserToken.first()
            }
        } catch (e: Exception) {
            null
        }

        val request = if (!token.isNullOrBlank() && token != "Unknown") {
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()

        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}