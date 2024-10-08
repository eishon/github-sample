package com.arctrix.githubsample.data.repository.helper

import com.arctrix.githubsample.util.interceptors.NoConnectivityException
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.Constants
import retrofit2.HttpException

object RepositoryHelper {

    /**
     * Execute the network call and return the result after wrapping based on the status of the API call
     */
    suspend fun <T> execute(retrievalAction: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(retrievalAction())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> ApiResult.HTTPError(
                    throwable.code(),
                    throwable.localizedMessage
                )
                is NoConnectivityException -> ApiResult.NetworkError
                else -> ApiResult.GenericError(0, Constants.GENERIC_ERROR_MESSAGE)
            }
        }
    }
}