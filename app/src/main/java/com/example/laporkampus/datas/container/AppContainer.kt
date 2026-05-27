package com.example.laporkampus.datas.container

import android.content.Context
import com.example.laporkampus.dataStore
import com.example.laporkampus.datas.interceptors.TokenInterceptor
import com.example.laporkampus.datas.repositories.AuthenticationRepository
import com.example.laporkampus.datas.repositories.AuthenticationRepositoryInterface
import com.example.laporkampus.datas.repositories.ReportStaffRepository
import com.example.laporkampus.datas.repositories.ReportStaffRepositoryInterface
import com.example.laporkampus.datas.repositories.UserRepository
import com.example.laporkampus.datas.repositories.UserRepositoryInterface
import com.example.laporkampus.datas.services.AuthenticationService
import com.example.laporkampus.datas.services.ReportStaffService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainerInterface {
    val authRepository: AuthenticationRepositoryInterface
    val userRepository: UserRepositoryInterface
    val reportStaffRepository: ReportStaffRepositoryInterface
}

class AppContainer(
    private val context: Context,
): AppContainerInterface {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"
    }

    override val userRepository: UserRepositoryInterface by lazy {
        UserRepository(dataStore = context.dataStore)
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val tokenInterceptor = TokenInterceptor(userRepository)

        OkHttpClient.Builder().addInterceptor(logging).addInterceptor(tokenInterceptor).build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    //Services
    private val authApi: AuthenticationService by lazy { retrofit.create(AuthenticationService::class.java) }

    private val reportStaffApi: ReportStaffService by lazy { retrofit.create(ReportStaffService::class.java) }

    // Repositories
    override val authRepository: AuthenticationRepository by lazy {
        AuthenticationRepository(authApi)
    }

    override val reportStaffRepository: ReportStaffRepository by lazy {
        ReportStaffRepository(reportStaffApi)
    }
}