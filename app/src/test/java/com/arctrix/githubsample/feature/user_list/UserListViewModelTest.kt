package com.arctrix.githubsample.feature.user_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.UserItem
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
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(GitHubRepository::class.java)

    private lateinit var sut: UserListViewModel

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

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = UnconfinedTestDispatcher())
        MockitoAnnotations.openMocks(this)

        sut = UserListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load all users is called when search key is empty`() = runTest {
        // when
        sut.loadUsers()

        // then
        verify(repository).getUsers()
    }

    @Test
    fun `load searched user is called when search key has value`() = runTest {
        // when
        sut.loadUsers("dummy")

        // then
        verify(repository).getSearchUsers("dummy")
    }

    @Test
    fun `get users list when api response is success`() = runTest {
        // given
        `when`(repository.getUsers()).thenReturn(ApiResult.Success(users))

        // when
        sut.loadUsers()

        // then
        assertEquals(users, sut.uiState.value.users)
    }

    @Test
    fun `get searched users list when api response is success`() = runTest {
        // given
        `when`(repository.getSearchUsers("dummy")).thenReturn(
            ApiResult.Success(
                SearchUserResponse(
                    3,
                    users
                )
            )
        )

        // when
        sut.loadUsers("dummy")

        // then
        assertEquals(users, sut.uiState.value.users)
    }

    @Test
    fun `triggers http error when there is an api error`() = runTest {
        // given
        val errorBody = """
            { "error": "No data found" }
        """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
        `when`(repository.getUsers()).thenReturn(
            ApiResult.HTTPError(
                404,
                errorBody.string()
            )
        )

        // when
        sut.loadUsers()

        // then
        assertEquals("HTTP Error occurred", sut.uiState.value.error)
    }

    @Test
    fun `triggers generic error when there is an error`() = runTest {
        // given
        `when`(repository.getUsers()).thenReturn(
            ApiResult.GenericError(0, "Error occurred")
        )

        // when
        sut.loadUsers()

        // then
        assertEquals("Error occurred", sut.uiState.value.error)
    }
}