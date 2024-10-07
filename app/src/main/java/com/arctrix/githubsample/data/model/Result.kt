package com.arctrix.githubsample.data.model

/**
 * Sealed class to wrap the API call result
 */
sealed class Result<out T> {

    /**
     * If the API call is successful, it will be wrapped as Success<T>
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Generic error
     */
    data class GenericError(
        val code: Int? = null,
        val message: String? = null
    ) : Result<Nothing>()

    /**
     * If there is an error in the API call
     */
    data class HTTPError(
        val code: Int? = null,
        val message: String? = null
    ) : Result<Nothing>()

    /**
     * If the device is not connected to internet
     */
    data object NetworkError : Result<Nothing>()
}