package id.co.drakorid.tv.model

data class BaseResponse<T>(
    val status: Boolean?,
    val message: String?,
    val data: T?
)

data class BaseResponseList<T>(
    val status: Boolean?,
    val message: String?,
    val data: List<T>?,
    val meta: Meta?
)

data class Meta(
    val currentPage: Int?,
    val lastPage: Int?,
    val total: Int?
)
