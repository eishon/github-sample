package com.arctrix.githubsample.data.repository.helper

import com.arctrix.githubsample.util.interceptors.NoConnectivityException
import com.arctrix.githubsample.data.model.Result
import com.arctrix.githubsample.util.Constants
import retrofit2.HttpException

object RepositoryHelper {

    /**
     * Execute the network call and return the result after wrapping based on the status of the API call
     */
    suspend fun <T> execute(retrievalAction: suspend () -> T): Result<T> {
        return try {
            Result.Success(retrievalAction())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> Result.HTTPError(
                    throwable.code(),
                    throwable.localizedMessage
                )
                is NoConnectivityException -> Result.NetworkError
                else -> Result.GenericError(0, Constants.GENERIC_ERROR_MESSAGE)
            }
        }
    }
}