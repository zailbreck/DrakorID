package id.co.drakorid.tv.util

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val message: String, val code: Int? = null) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun errorMessage(): String? = when (this) {
        is Error -> message
        else -> null
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String, code: Int? = null) = Error(message, code)
        fun loading() = Loading
    }
}
