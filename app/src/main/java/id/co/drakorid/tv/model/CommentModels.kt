package id.co.drakorid.tv.model

data class CommentEntity(
    val id: String?,
    val content: String?,
    val user: UserEntity?,
    val createdAt: String?,
    val totalLikes: Int?,
    val isLiked: Boolean?,
    val replies: List<CommentEntity>?
)

data class CommentCountEntity(val total: Int?)
data class CommentLikeEntity(val isLiked: Boolean?, val totalLikes: Int?)
data class CreateCommentRequest(val movieId: String, val content: String)
data class CreateReplyRequest(val commentId: String, val content: String)
data class LikeRequest(val id: String)
