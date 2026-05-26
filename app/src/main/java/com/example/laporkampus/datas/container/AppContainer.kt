package com.example.laporkampus.datas.container

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.laporkampus.datas.repositories.AuthenticationRepository
import com.example.laporkampus.datas.services.AuthenticationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(
    private val dataStore: DataStore<Preferences>
) {
    // 1. Setup logging to watch JSON bodies drop in your logcat console
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. Build the client with global header requirements
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // 3. Initialize the global Network Engine
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.4.99:3000/") // Ensure your local API server ip matches!
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 4. Create the API instance from your Service interface
    private val authApi: AuthenticationService by lazy {
        retrofit.create(AuthenticationService::class.java)
    }

    // 5. Expose the Single Source of Truth Repository with network & local storage attached
//    val authenticationRepository: AuthenticationRepository by lazy {
//        AuthenticationRepository(authService = authApi, dataStore = dataStore)
//    }
}