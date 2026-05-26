package com.example.laporkampus

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.laporkampus.datas.container.AppContainer

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_data"
)

class LaporKampusApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(dataStore)
    }
}