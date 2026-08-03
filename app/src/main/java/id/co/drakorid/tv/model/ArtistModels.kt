package id.co.drakorid.tv.model

data class ArtistEntity(
    val id: String?,
    val name: String?,
    val image: String?,
    val banner: String?,
    val description: String?,
    val totalMovies: Int?,
    val isFollowed: Boolean?,
    val rating: Double?
)

data class FollowRequest(val artistId: String)
data class ArtistFollowEntity(val isFollowed: Boolean?)
data class ArtistFollowStatusEntity(val isFollowed: Boolean?)
data class RatingRequest(val artistId: String, val rating: Float)

data class ReviewEntity(
    val id: String?,
    val content: String?,
    val user: UserEntity?,
    val rating: Float?,
    val createdAt: String?
)
