package com.example.nutriton.data.api

/**
 * Clase sellada para representar diferentes estados de una operación de red
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

/**
 * Estados de carga para la UI
 */
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)

/**
 * Clase para representar errores específicos de la API
 */
sealed class ApiError : Exception() {
    object NetworkError : ApiError()
    object ServerError : ApiError()
    data class HttpError(val code: Int, val errorMessage: String) : ApiError()
    data class UnknownError(val originalException: Throwable) : ApiError()
}

/**
 * Extensión para convertir excepciones en ApiError
 */
fun Throwable.toApiError(): ApiError {
    return when (this) {
        is java.net.UnknownHostException,
        is java.net.ConnectException,
        is java.net.SocketTimeoutException -> ApiError.NetworkError
        is retrofit2.HttpException -> ApiError.HttpError(this.code(), this.message())
        else -> ApiError.UnknownError(this)
    }
}
