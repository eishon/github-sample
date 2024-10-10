package com.arctrix.githubsample.feature.user_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserRepo
import com.arctrix.githubsample.data.repository.GitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(GitHubRepository::class.java)

    private lateinit var sut: UserDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = UnconfinedTestDispatcher())
        MockitoAnnotations.openMocks(this)

        sut = UserDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load users details was called`() = runTest {
        // when
        sut.loadUserDetails(anyString())

        // then
        verify(repository).getUserDetails(anyString())
    }


    @Test
    fun `get users details when api response is success`() = runTest {
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
        `when`(repository.getUserDetails(anyString())).thenReturn(ApiResult.Success(userDetail))

        // when
        sut.loadUserDetails(anyString())

        // then
        assertEquals(userDetail, sut.uiState.value.userDetail)
    }

    @Test
    fun `get users repos list when api response is success`() = runTest {
        // given
        val userRepos = listOf(
            UserRepo(
                name = "Android-Samples",
                description = "This repository is for android application code samples.",
                htmlUrl = "https://github.com/eishon/Android-Samples",
                stargazersCount = 1,
                watchersCount = 1,
                language = "Kotlin",
                fork = false
            ),
            UserRepo(
                name = "Appium-Automation-Testing",
                description = null,
                htmlUrl = "https://github.com/eishon/Appium-Automation-Testing",
                language = "Java",
                stargazersCount = 1,
                watchersCount = 2,
                fork = true
            ),
            UserRepo(
                name = "Arduino-Speed-Measurement",
                description = "An simple Arduino setup for measuring speed of a moving object and Android app for showing the speed.",
                htmlUrl = "https://github.com/eishon/Arduino-Speed-Measurement",
                language = "Java",
                stargazersCount = 1,
                watchersCount = 1,
                fork = false
            )
        )
        `when`(repository.getUserRepos(anyString())).thenReturn(ApiResult.Success(userRepos))

        // when
        sut.loadUserRepos(anyString())

        // then
        assertEquals(userRepos, sut.uiState.value.userRepos)
    }

    @Test
    fun `triggers http error when there is an api error`() = runTest {
        // given
        val errorBody = """
            { "error": "No data found" }
        """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
        `when`(repository.getUserDetails(anyString())).thenReturn(
            ApiResult.HTTPError(
                404,
                errorBody.string()
            )
        )

        // when
        sut.loadUserDetails(anyString())

        // then
        assertEquals("HTTP Error occurred", sut.uiState.value.error)
    }

    @Test
    fun `triggers generic error when there is an error`() = runTest {
        // given
        `when`(repository.getUserDetails(anyString())).thenReturn(
            ApiResult.GenericError(0, "Error occurred")
        )

        // when
        sut.loadUserDetails(anyString())

        // then
        assertEquals("Error occurred", sut.uiState.value.error)
    }
}