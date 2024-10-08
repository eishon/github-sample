package com.arctrix.githubsample.data.model

/**
 * Sealed class to wrap the API call result
 */
sealed class ApiResult<out T> {

    /**
     * If the API call is successful, it will be wrapped as Success<T>
     */
    data class Success<out T>(val data: T) : ApiResult<T>()

    /**
     * Generic error
     */
    data class GenericError(
        val code: Int? = null,
        val message: String? = null
    ) : ApiResult<Nothing>()

    /**
     * If there is an error in the API call
     */
    data class HTTPError(
        val code: Int? = null,
        val message: String? = null
    ) : ApiResult<Nothing>()

    /**
     * If the device is not connected to internet
     */
    data object NetworkError : ApiResult<Nothing>()
}