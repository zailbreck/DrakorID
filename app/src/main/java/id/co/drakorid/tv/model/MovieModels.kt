package id.co.drakorid.tv.model

data class MovieEntity(
    val id: String?,
    val title: String?,
    val poster: String?,
    val banner: String?,
    val rating: Double?,
    val year: String?,
    val duration: String?,
    val description: String?,
    val genre: String?,
    val country: String?,
    val totalEpisodes: Int?,
    val status: String?,
    val isFavorite: Boolean?,
    val isNotified: Boolean?,
    val artists: List<ArtistEntity>?,
    val episodes: List<EpisodeEntity>?
)

data class EpisodeEntity(
    val id: String?,
    val title: String?,
    val episodeNumber: Int?,
    val thumbnail: String?,
    val duration: String?
)

data class PlayHistoryEntity(
    val id: String?,
    val movie: MovieEntity?,
    val episode: EpisodeEntity?,
    val progress: Long?,
    val updatedAt: String?
)

data class FavRequest(val id: String)
data class FavoriteEntity(val isFavorite: Boolean?)
data class NotifRequest(val id: String)
data class NotifSubsEntity(val isNotified: Boolean?)
