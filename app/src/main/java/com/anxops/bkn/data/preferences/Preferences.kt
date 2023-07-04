/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BknDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("bikenance_data_store")
        val AUTH_USER = stringPreferencesKey("AUTH_USER")
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val FIREBASE_TOKEN = stringPreferencesKey("FIREBASE_TOKEN")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun saveAuthTokens(token: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun deleteAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(FIREBASE_TOKEN)
        }
    }

    suspend fun getAuthToken(): String? {
        return context.dataStore.data.first()[AUTH_TOKEN]
    }

    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN]
    }

    suspend fun saveFirebaseToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[FIREBASE_TOKEN] = token
        }
    }

    suspend fun getFirebaseToken(): String? {
        return context.dataStore.data.first()[FIREBASE_TOKEN]
    }

    suspend fun saveAuthUser(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_USER] = token
        }
    }

    suspend fun getAuthUser(): String? {
        return context.dataStore.data.first()[AUTH_USER]
    }

    suspend fun getAuthUserOrFail(): String {
        return context.dataStore.data.first()[AUTH_USER] ?: throw Exception("Auth user not found")
    }


}