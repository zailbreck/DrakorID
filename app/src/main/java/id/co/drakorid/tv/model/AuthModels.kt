package id.co.drakorid.tv.model

import com.google.gson.annotations.SerializedName

// ── Requests ──

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class GoogleValidateRequest(val code: String)
data class UpdateProfileRequest(val name: String, val email: String? = null)
data class UpdatePasswordRequest(val oldPassword: String, val newPassword: String)
data class UpdatePictureRequest(val picture: String)
data class ResetPasswordRequest(val email: String)

// ── Responses ──

data class AuthEntity(
    @SerializedName("jwt_token") val jwtToken: String?,
    val user: UserEntity?
)

data class UserEntity(
    val id: Int?,
    val name: String?,
    val email: String?,
    val picture: String?,
    @SerializedName("is_premium") val isPremium: Boolean?,
    val role: String?
)

data class AuthGoogleEntity(
    val url: String?
)
