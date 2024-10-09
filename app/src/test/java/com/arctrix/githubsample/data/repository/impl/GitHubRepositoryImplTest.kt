package com.arctrix.githubsample.data.repository.impl

import com.arctrix.githubsample.data.api.GitHubApi
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserItem
import com.arctrix.githubsample.data.model.github.UserRepo
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response

class GitHubUserRepositoryImplTest {
    private val mockApi = mock(GitHubApi::class.java)

    private val sut = GitHubRepositoryImpl(mockApi)

    private val users = listOf(
        UserItem(
            id = 1,
            login = "User1",
            avatarUrl = "https://avatars.githubusercontent.com/u/31154892?v=4",
            htmlUrl = "https://github.com/User1"
        ),
        UserItem(
            id = 2,
            login = "User2",
            avatarUrl = "https://avatars.githubusercontent.com/u/94143866?v=4",
            htmlUrl = "https://github.com/User2"
        ),
        UserItem(
            id = 3,
            login = "User3",
            avatarUrl = "https://avatars.githubusercontent.com/u/18392689?v=4",
            htmlUrl = "https://github.com/User3"
        )
    )

    @Test
    fun `when get users api response is success`(): Unit = runTest {
        // given
        `when`(mockApi.getUsers()).thenReturn(users)

        // when
        val actual = sut.getUsers()

        // then
        assertTrue(actual is ApiResult.Success)
        assertEquals(users, (actual as ApiResult.Success).data)
    }

    @Test
    fun `when api response is failure`(): Unit = runTest {
        // given
        val errorBody = """
            { "error": "No data found" }
        """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
        `when`(mockApi.getUsers()).thenThrow(
            HttpException(Response.error<Any>(404, errorBody))
        )

        // when
        val actual = sut.getUsers()

        // then
        assertTrue(actual is ApiResult.HTTPError)
        assertEquals(404, (actual as ApiResult.HTTPError).code)
    }

    @Test
    fun `when get searched users api response is success`(): Unit = runTest {
        // given
        val searchUserResponse = SearchUserResponse(
            totalCount = 3,
            items = users
        )
        `when`(mockApi.getUsersSearch(anyString())).thenReturn(searchUserResponse)

        // when
        val actual = sut.getSearchUsers(anyString())

        // then
        assertTrue(actual is ApiResult.Success)
        assertEquals(searchUserResponse, (actual as ApiResult.Success).data)
    }

    @Test
    fun `when get user details api response is success`(): Unit = runTest {
        // given
        val userDetail = UserDetail(
            login = "User1",
            avatarUrl = "https://avatars.githubusercontent.com/u/31154892?v=4",
            htmlUrl = "https://github.com/User1",
            reposUrl = "https://api.github.com/users/User1/repos",
            name = "lazy_potato_0_0_",
            company = "GitHub",
            location = "Tokyo, Japan",
            bio = "Mobile Application Developer (Android, iOS, Flutter, Unity)",
            publicRepos = 29,
            followers = 5,
            following = 3
        )
        `when`(mockApi.getUsersDetails(anyString())).thenReturn(userDetail)

        // when
        val actual = sut.getUserDetails(anyString())

        // then
        assertTrue(actual is ApiResult.Success)
        assertEquals(userDetail, (actual as ApiResult.Success).data)
    }

    @Test
    fun `when get user repos api response is success`(): Unit = runTest {
        // given
        val userRepos = listOf(
            UserRepo(
                name = "Android-Samples",
                description = "This repository is for android application code samples.",
                htmlUrl = "https://github.com/eishon/Android-Samples",
                stargazersCount = 1,
                watchersCount = 1,
                language = "Kotlin"
            ),
            UserRepo(
                name = "Appium-Automation-Testing",
                description = null,
                htmlUrl = "https://github.com/eishon/Appium-Automation-Testing",
                language = "Java",
                stargazersCount = 0,
                watchersCount = 0
            ),
            UserRepo(
                name = "Arduino-Speed-Measurement",
                description = "An simple Arduino setup for measuring speed of a moving object and Android app for showing the speed.",
                htmlUrl = "https://github.com/eishon/Arduino-Speed-Measurement",
                language = "Java",
                stargazersCount = 1,
                watchersCount = 1
            )
        )
        `when`(mockApi.getUserRepos(anyString())).thenReturn(userRepos)

        // when
        val actual = sut.getUserRepos(anyString())

        // then
        assertTrue(actual is ApiResult.Success)
        assertEquals(userRepos, (actual as ApiResult.Success).data)
    }
}