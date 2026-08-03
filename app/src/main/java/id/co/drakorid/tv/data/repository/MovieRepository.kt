package id.co.drakorid.tv.data.repository

import id.co.drakorid.tv.model.*
import id.co.drakorid.tv.network.ApiService
import id.co.drakorid.tv.util.AppResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: ApiService
) {
    private suspend fun <T> safeCall(call: suspend () -> retrofit2.Response<BaseResponse<T>>): AppResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == true && body.data != null) {
                    AppResult.success(body.data)
                } else {
                    AppResult.error(body?.message ?: "Data tidak tersedia")
                }
            } else {
                AppResult.error("Server error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            AppResult.error(e.message ?: "Koneksi gagal")
        }
    }

    private suspend fun <T> safeCallList(call: suspend () -> retrofit2.Response<BaseResponseList<T>>): AppResult<List<T>> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == true && body.data != null) {
                    AppResult.success(body.data)
                } else {
                    AppResult.success(emptyList())
                }
            } else {
                AppResult.error("Server error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            AppResult.error(e.message ?: "Koneksi gagal")
        }
    }

    suspend fun getLatest(page: Int = 1) = safeCallList { api.getLatestMovies(page) }
    suspend fun getOngoing(page: Int = 1) = safeCallList { api.getOngoingMovies(page) }
    suspend fun getTrending(page: Int = 1) = safeCallList { api.getTrendingMovies(page) }
    suspend fun getPopular(page: Int = 1) = safeCallList { api.getPopularMovies(page) }
    suspend fun getSlider() = safeCallList { api.getSliderMovies() }
    suspend fun getRecommended(page: Int = 1) = safeCallList { api.getRecommended(page) }
    suspend fun search(query: String, page: Int = 1) = safeCallList { api.searchMovies(query, page) }
    suspend fun getMovieInfo(movieId: String) = safeCall { api.getMovieInfo(movieId) }
    suspend fun getEpisodes(movieId: String) = safeCallList { api.getEpisodes(movieId) }
    suspend fun getCategories() = safeCallList { api.getCategories() }
    suspend fun getByCategory(category: String, page: Int = 1) = safeCallList { api.getMoviesByCategory(category, page) }
    suspend fun getByGenre(genre: String, page: Int = 1) = safeCallList { api.getMoviesByGenre(genre, page) }
    suspend fun getFavorites(page: Int = 1) = safeCallList { api.getFavorites(page) }

    suspend fun addFavorite(movieId: String): AppResult<Unit> = safeCall { api.addFavorite(FavRequest(movieId)) }
    suspend fun removeFavorite(movieId: String): AppResult<Unit> = safeCall { api.removeFavorite(FavRequest(movieId)) }

    suspend fun isFavorite(movieId: String): Boolean {
        return when (val result = safeCall { api.isFavorite(movieId) }) {
            is AppResult.Success -> result.data?.isFavorite ?: false
            else -> false
        }
    }

    suspend fun getPlayUrl(episodeId: String): AppResult<LinkEntity> {
        val max = safeCall { api.getPlayMax(episodeId) }
        if (max is AppResult.Success && max.data?.url != null) return max
        val fast = safeCall { api.getPlayFast(episodeId) }
        if (fast is AppResult.Success && fast.data?.url != null) return fast
        return AppResult.error("URL video tidak tersedia")
    }
}
