package com.arctrix.githubsample.feature.user_details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val gitHubUserRepository: GitHubRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserDetailsUiState> =
        MutableStateFlow(UserDetailsUiState())
    val uiState: StateFlow<UserDetailsUiState>
        get() = _uiState.asStateFlow()

    fun loadUserDetails(userId: String) {
        viewModelScope.launch {
            displayLoading()
            when (val result = gitHubUserRepository.getUserDetails(userId)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            userDetail = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    loadUserRepos(userId)
                }

                else -> handleError(result)
            }
        }
    }

    @VisibleForTesting
    fun loadUserRepos(userId: String) {
        viewModelScope.launch {
            when (val result = gitHubUserRepository.getUserRepos(userId)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            userRepos = result.data,
                            error = null
                        )
                    }
                }

                else -> handleError(result)
            }
        }
    }

    private fun displayLoading() {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
    }

    private fun handleError(result: ApiResult<*>) {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = when (result) {
                    is ApiResult.NetworkError -> "No Network"
                    is ApiResult.HTTPError -> "HTTP Error occurred"
                    else -> "Error occurred"
                }
            )
        }
    }
}