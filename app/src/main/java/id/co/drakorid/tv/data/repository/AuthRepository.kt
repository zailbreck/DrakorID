package id.co.drakorid.tv.data.repository

import id.co.drakorid.tv.model.*
import id.co.drakorid.tv.network.ApiService
import id.co.drakorid.tv.network.TokenManager
import id.co.drakorid.tv.util.AppResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): AppResult<AuthEntity> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == true && body.data != null) {
                    body.data.jwtToken?.let { tokenManager.saveToken(it) }
                    AppResult.success(body.data)
                } else {
                    AppResult.error(body?.message ?: "Login gagal")
                }
            } else {
                AppResult.error("Server error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            AppResult.error(e.message ?: "Koneksi gagal")
        }
    }

    suspend fun register(name: String, email: String, password: String): AppResult<AuthEntity> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == true && body.data != null) {
                    body.data.jwtToken?.let { tokenManager.saveToken(it) }
                    AppResult.success(body.data)
                } else {
                    AppResult.error(body?.message ?: "Registrasi gagal")
                }
            } else {
                AppResult.error("Server error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            AppResult.error(e.message ?: "Koneksi gagal")
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    suspend fun isLoggedIn(): Boolean =
        tokenManager.getToken() != null
}
