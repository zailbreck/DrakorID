package id.co.drakorid.tv.network

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    fun observeToken(): Flow<String?>
}
