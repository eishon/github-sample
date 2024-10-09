package com.arctrix.githubsample.data.repository.helper

import com.arctrix.githubsample.data.Constants
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.util.interceptors.NoConnectivityException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class RepositoryHelperTest {

    @Test
    fun `when API result is success`() = runTest {
        // given
        fun successResponse() = "Success"

        // when
        val result = RepositoryHelper.execute<String> {
            successResponse()
        }

        // then
        assertTrue(result is ApiResult.Success<String>)
        assertEquals("Success", (result as ApiResult.Success<String>).data)
    }

    @Test
    fun `when API result is http error`() = runTest {
        // given
        val errorBody = """
            { "error":  "No data found" }
        """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())

        // when
        val result = RepositoryHelper.execute {
            throw HttpException(Response.error<Any>(404, errorBody))
        }

        // then
        assertTrue(result is ApiResult.HTTPError)
        assertEquals(404, (result as ApiResult.HTTPError).code)
    }

    @Test
    fun `when not connected to internet`() = runTest {
        // when
        val result = RepositoryHelper.execute {
            throw NoConnectivityException()
        }

        // then
        assertTrue(result is ApiResult.NetworkError)
    }

    @Test
    fun `when generic error occurs`() = runTest {
        // when
        val result = RepositoryHelper.execute {
            throw RuntimeException()
        }

        // then
        assertTrue(result is ApiResult.GenericError)
        assertEquals(0, (result as ApiResult.GenericError).code)
        assertEquals(Constants.GENERIC_ERROR_MESSAGE, result.message)
    }
}