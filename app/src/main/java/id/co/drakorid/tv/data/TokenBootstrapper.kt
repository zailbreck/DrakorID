package id.co.drakorid.tv.data

import id.co.drakorid.tv.model.FreeTokenEntity
import id.co.drakorid.tv.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import com.google.gson.Gson

@Singleton
class TokenBootstrapper @Inject constructor(
    private val tokenManager: TokenManager
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /**
     * Fetch anonymous free-token from API and persist it.
     * Called once at app startup.
     */
    suspend fun ensureToken() {
        // Already have a token (user logged in or cached free token)
        val existing = tokenManager.getToken()
        if (!existing.isNullOrBlank()) return

        try {
            val token = fetchFreeToken() ?: return
            tokenManager.saveToken(token)
        } catch (e: Exception) {
            // Silent — interceptor will retry next request
        }
    }

    private suspend fun fetchFreeToken(): String? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://api.drakor.la/v2/app/free-token")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string() ?: return@withContext null
                val parsed = gson.fromJson(body, FreeTokenEntity::class.java)
                parsed.data?.jwt_token
            }
        } catch (e: Exception) {
            null
        }
    }
}
