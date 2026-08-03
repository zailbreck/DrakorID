package id.co.drakorid.tv.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.drakorid.tv.network.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "drakorid_tv_prefs")

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("jwt_token")
    }

    override suspend fun getToken(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_TOKEN]
        }.let { flow ->
            var token: String? = null
            flow.collect { token = it }
            token
        }
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
        }
    }

    override suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_TOKEN)
        }
    }

    override fun observeToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_TOKEN]
        }
    }
}
