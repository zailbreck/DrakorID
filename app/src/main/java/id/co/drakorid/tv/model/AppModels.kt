package id.co.drakorid.tv.model

// ── App Config ──

data class AppConfigEntity(
    val ads: SystemAdsEntity?,
    val system: SystemAppEntity?
)

data class SystemAdsEntity(val showAds: Boolean?)
data class SystemAppEntity(val maintenance: Boolean?, val forceUpdate: Boolean?, val version: String?)

data class AppInfoEntity(
    val version: String?,
    val whatsNew: String?,
    val contactEmail: String?
)

data class UnderReviewEntity(val isReview: Boolean?)

// ── Categories ──

data class CategoryEntity(
    val id: String?,
    val name: String?,
    val totalMovies: Int?
)

// ── Banner / Promo ──

data class BannerEntity(
    val id: String?,
    val image: String?,
    val movieId: String?,
    val url: String?,
    val type: String?
)

data class PopupEntity(
    val id: String?,
    val image: String?,
    val url: String?,
    val type: String?
)

data class PromoEntity(
    val id: String?,
    val title: String?,
    val image: String?,
    val url: String?,
    val description: String?
)

data class FloatingEntity(
    val id: String?,
    val image: String?,
    val url: String?,
    val type: String?
)

// ── OST ──

data class OstEntity(
    val id: String?,
    val title: String?,
    val image: String?,
    val description: String?,
    val movies: List<MovieEntity>?
)

data class OstMusicEntity(
    val id: String?,
    val title: String?,
    val url: String?,
    val artist: String?
)
