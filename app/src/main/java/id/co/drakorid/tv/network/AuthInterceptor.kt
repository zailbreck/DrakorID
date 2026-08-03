package id.co.drakorid.tv.network

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getToken() }
        val builder = chain.request().newBuilder()
            .addHeader("Accept", "application/json")

        // API ini pakai raw token tanpa prefix "Bearer "
        if (token != null) {
            builder.addHeader("Authorization", token)
        }

        return chain.proceed(builder.build())
    }
}
