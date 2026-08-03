package id.co.drakorid.tv.data.repository

import id.co.drakorid.tv.model.ArtistEntity
import id.co.drakorid.tv.model.FollowRequest
import id.co.drakorid.tv.model.ReviewEntity
import id.co.drakorid.tv.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getAll(page: Int = 1): List<ArtistEntity> =
        api.getAllArtists(page).body()?.data ?: emptyList()

    suspend fun getForIndex(): List<ArtistEntity> =
        api.getArtistForIndex().body()?.data ?: emptyList()

    suspend fun getInfo(artistId: String): ArtistEntity? =
        api.getArtistInfo(artistId).body()?.data

    suspend fun follow(artistId: String) =
        api.followArtist(FollowRequest(artistId))

    suspend fun getReviews(artistId: String, page: Int = 1): List<ReviewEntity> =
        api.getArtistReviews(artistId, page).body()?.data ?: emptyList()
}
