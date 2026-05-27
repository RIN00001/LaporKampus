package com.example.laporkampus.datas.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepositoryInterface {
    val currentUserToken: Flow<String>
    val currentUsername: Flow<String>

    suspend fun saveUserToken(token: String)
    suspend fun saveUsername(name: String)

    suspend fun logout()
}

class UserRepository(private val dataStore: DataStore<Preferences>): UserRepositoryInterface {
    override val currentUserToken: Flow<String> = dataStore.data.map { preferences -> preferences[USER_TOKEN] ?: "" }
    override val currentUsername: Flow<String> = dataStore.data.map { preferences -> preferences[USERNAME] ?: "" }

    override suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences -> preferences[USER_TOKEN] = token }
    }

    override suspend fun saveUsername(name: String) {
        dataStore.edit { preferences -> preferences[USERNAME] = name }
    }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {
        val USER_TOKEN = stringPreferencesKey("token")
        val USERNAME = stringPreferencesKey("username")
    }
}