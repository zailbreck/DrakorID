package id.co.drakorid.tv.data.repository

import id.co.drakorid.tv.model.*
import id.co.drakorid.tv.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getComments(movieId: String, page: Int = 1): List<CommentEntity> =
        api.getComments(movieId, page).body()?.data ?: emptyList()

    suspend fun getCount(movieId: String): Int =
        api.getCommentCount(movieId).body()?.data?.total ?: 0

    suspend fun send(movieId: String, content: String): CommentEntity? =
        api.sendComment(CreateCommentRequest(movieId, content)).body()?.data

    suspend fun like(commentId: String) =
        api.likeComment(LikeRequest(commentId))

    suspend fun getReplies(commentId: String, page: Int = 1): List<CommentEntity> =
        api.getReplies(commentId, page).body()?.data ?: emptyList()

    suspend fun sendReply(commentId: String, content: String): CommentEntity? =
        api.sendReply(CreateReplyRequest(commentId, content)).body()?.data
}
