package id.co.drakorid.tv.network

import id.co.drakorid.tv.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ═══════════════════════════════════════════
    // AUTH
    // ═══════════════════════════════════════════

    @POST("v2/users/login")
    suspend fun login(@Body body: LoginRequest): Response<BaseResponse<AuthEntity>>

    @POST("v2/users/register")
    suspend fun register(@Body body: RegisterRequest): Response<BaseResponse<AuthEntity>>

    @GET("v2/users/login-with-google/get")
    suspend fun getGoogleLoginUrl(): Response<BaseResponse<AuthGoogleEntity>>

    @POST("v2/users/login-with-google/validate")
    suspend fun validateGoogleLogin(@Body body: GoogleValidateRequest): Response<BaseResponse<AuthEntity>>

    @GET("v2/users/info")
    suspend fun getUserInfo(): Response<BaseResponse<UserEntity>>

    @POST("v2/users/update-profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): Response<BaseResponse<UserEntity>>

    @POST("v2/users/update-password")
    suspend fun updatePassword(@Body body: UpdatePasswordRequest): Response<BaseResponse<Unit>>

    @POST("v2/users/request-reset-password")
    suspend fun requestResetPassword(@Body body: ResetPasswordRequest): Response<BaseResponse<Unit>>

    @POST("v2/users/update-picture")
    suspend fun updatePicture(@Body body: UpdatePictureRequest): Response<BaseResponse<UserEntity>>

    @GET("v2/app/free-token")
    suspend fun getFreeToken(): Response<FreeTokenEntity>

    // ═══════════════════════════════════════════
    // MOVIES
    // ═══════════════════════════════════════════

    @GET("v2/movies/latest")
    suspend fun getLatestMovies(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/ongoing")
    suspend fun getOngoingMovies(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/trending")
    suspend fun getTrendingMovies(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/popular-on1")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/latest-on1")
    suspend fun getLatestOn1(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/search")
    suspend fun searchMovies(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/search-on1")
    suspend fun searchOn1(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/get-info")
    suspend fun getMovieInfo(@Query("id") movieId: String): Response<BaseResponse<MovieEntity>>

    @GET("v2/movies/get-episodes")
    suspend fun getEpisodes(@Query("id") movieId: String): Response<BaseResponseList<EpisodeEntity>>

    @GET("v2/movies/category")
    suspend fun getMoviesByCategory(
        @Query("category") category: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/slider")
    suspend fun getSliderMovies(): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/by-artist")
    suspend fun getMoviesByArtist(
        @Query("artistId") artistId: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/history")
    suspend fun getPlayHistory(@Query("page") page: Int = 1): Response<BaseResponseList<PlayHistoryEntity>>

    // ── Index endpoints ──

    @GET("v2/movies/index/by-genre")
    suspend fun getMoviesByGenre(
        @Query("genre") genre: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/index/origin-drama")
    suspend fun getOriginDrama(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/index/origin-film")
    suspend fun getOriginFilm(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/index/recommended")
    suspend fun getRecommended(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/index/variety-show")
    suspend fun getVarietyShow(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    @GET("v2/movies/index/by-artist")
    suspend fun getIndexByArtist(
        @Query("artistId") artistId: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<MovieEntity>>

    // ── Favorites ──

    @POST("v2/movies/fav/add")
    suspend fun addFavorite(@Body body: FavRequest): Response<BaseResponse<Unit>>

    @POST("v2/movies/fav/remove")
    suspend fun removeFavorite(@Body body: FavRequest): Response<BaseResponse<Unit>>

    @GET("v2/movies/fav/is-fav")
    suspend fun isFavorite(@Query("id") movieId: String): Response<BaseResponse<FavoriteEntity>>

    @GET("v2/movies/favorites")
    suspend fun getFavorites(@Query("page") page: Int = 1): Response<BaseResponseList<MovieEntity>>

    // ── Notifications ──

    @POST("v2/movies/notif/add")
    suspend fun addNotification(@Body body: NotifRequest): Response<BaseResponse<Unit>>

    @POST("v2/movies/notif/remove")
    suspend fun removeNotification(@Body body: NotifRequest): Response<BaseResponse<Unit>>

    @GET("v2/movies/notif/is-notif")
    suspend fun isNotified(@Query("id") movieId: String): Response<BaseResponse<NotifSubsEntity>>

    // ═══════════════════════════════════════════
    // COMMENTS (komentar)
    // ═══════════════════════════════════════════

    @GET("v2/komentar/get")
    suspend fun getComments(
        @Query("movieId") movieId: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<CommentEntity>>

    @GET("v2/komentar/count")
    suspend fun getCommentCount(@Query("movieId") movieId: String): Response<BaseResponse<CommentCountEntity>>

    @POST("v2/komentar/send")
    suspend fun sendComment(@Body body: CreateCommentRequest): Response<BaseResponse<CommentEntity>>

    @POST("v2/komentar/liked-unliked")
    suspend fun likeComment(@Body body: LikeRequest): Response<BaseResponse<CommentLikeEntity>>

    @GET("v2/komentar/balasan/get")
    suspend fun getReplies(
        @Query("commentId") commentId: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<CommentEntity>>

    @POST("v2/komentar/balasan/send")
    suspend fun sendReply(@Body body: CreateReplyRequest): Response<BaseResponse<CommentEntity>>

    @POST("v2/komentar/balasan/liked-unliked")
    suspend fun likeReply(@Body body: LikeRequest): Response<BaseResponse<CommentLikeEntity>>

    // ═══════════════════════════════════════════
    // ARTISTS
    // ═══════════════════════════════════════════

    @GET("v2/artist/get-all")
    suspend fun getAllArtists(@Query("page") page: Int = 1): Response<BaseResponseList<ArtistEntity>>

    @GET("v2/artist/info")
    suspend fun getArtistInfo(@Query("id") artistId: String): Response<BaseResponse<ArtistEntity>>

    @GET("v2/artist/get-for-index")
    suspend fun getArtistForIndex(): Response<BaseResponseList<ArtistEntity>>

    @GET("v2/artist/get-list")
    suspend fun getArtistList(@Query("page") page: Int = 1): Response<BaseResponseList<ArtistEntity>>

    @POST("v2/artist/follow-unfollow")
    suspend fun followArtist(@Body body: FollowRequest): Response<BaseResponse<ArtistFollowEntity>>

    @GET("v2/artist/is-followedF")
    suspend fun isArtistFollowed(@Query("artistId") artistId: String): Response<BaseResponse<ArtistFollowStatusEntity>>

    @POST("v2/artist/send-rating")
    suspend fun rateArtist(@Body body: RatingRequest): Response<BaseResponse<Unit>>

    @GET("v2/artist/ulasan")
    suspend fun getArtistReviews(
        @Query("artistId") artistId: String,
        @Query("page") page: Int = 1
    ): Response<BaseResponseList<ReviewEntity>>

    // ═══════════════════════════════════════════
    // LINKS / STREAMING
    // ═══════════════════════════════════════════

    @GET("v2/generate-link/play-max")
    suspend fun getPlayMax(@Query("episodeId") episodeId: String): Response<BaseResponse<LinkEntity>>

    @GET("v2/generate-link/play-fast")
    suspend fun getPlayFast(@Query("episodeId") episodeId: String): Response<BaseResponse<LinkEntity>>

    @GET("v2/generate-link/play-lite")
    suspend fun getPlayLite(@Query("episodeId") episodeId: String): Response<BaseResponse<LinkEntity>>

    @GET("v2/generate-link/download-fast")
    suspend fun getDownloadFast(@Query("episodeId") episodeId: String): Response<BaseResponse<LinkEntity>>

    @GET("v2/generate-link/download-lite")
    suspend fun getDownloadLite(@Query("episodeId") episodeId: String): Response<BaseResponse<LinkEntity>>

    // ═══════════════════════════════════════════
    // CATEGORIES
    // ═══════════════════════════════════════════

    @GET("v2/category/get-with-stats")
    suspend fun getCategories(): Response<BaseResponseList<CategoryEntity>>

    // ═══════════════════════════════════════════
    // OST (Soundtrack)
    // ═══════════════════════════════════════════

    @GET("v2/ost/get-all")
    suspend fun getAllOst(@Query("page") page: Int = 1): Response<BaseResponseList<OstEntity>>

    @GET("v2/ost/get-info")
    suspend fun getOstInfo(@Query("id") ostId: String): Response<BaseResponse<OstEntity>>

    @GET("v2/ost/get-ost-music")
    suspend fun getOstMusic(@Query("id") ostId: String): Response<BaseResponseList<OstMusicEntity>>

    // ═══════════════════════════════════════════
    // APP CONFIG
    // ═══════════════════════════════════════════

    @GET("v2/app/config")
    suspend fun getAppConfig(): Response<BaseResponse<AppConfigEntity>>

    @GET("v2/app/info")
    suspend fun getAppInfo(): Response<BaseResponse<AppInfoEntity>>

    @GET("v2/app/is-review")
    suspend fun isUnderReview(): Response<BaseResponse<UnderReviewEntity>>

    // ═══════════════════════════════════════════
    // PROMO / BANNER (content discovery only)
    // ═══════════════════════════════════════════

    @GET("v1/banner-promo/get")
    suspend fun getBannerPromo(): Response<BaseResponseList<BannerEntity>>

    @GET("v1/popup/get")
    suspend fun getPopup(): Response<BaseResponse<PopupEntity>>

    @GET("v2/promo/get")
    suspend fun getPromo(): Response<BaseResponse<PromoEntity>>

    @GET("v1/floating-promo/get")
    suspend fun getFloatingPromo(): Response<BaseResponse<FloatingEntity>>
}
