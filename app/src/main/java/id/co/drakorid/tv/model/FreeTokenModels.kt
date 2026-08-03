package id.co.drakorid.tv.model

data class FreeTokenEntity(
    val status: Int?,
    val rc: Int?,
    val message: String?,
    val data: FreeTokenData?,
    val ts: Long?
)

data class FreeTokenData(
    val jwt_token: String?
)
