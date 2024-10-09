package com.arctrix.githubsample.feature.user_list

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
class UserListViewModel @Inject constructor(
    private val gitHubUserRepository: GitHubRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserListUiState> =
        MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState>
        get() = _uiState.asStateFlow()

    fun loadUsers(searchKey: String = "") {
        if (searchKey.isEmpty()) {
            loadAllUsers()
        } else {
            loadSearchedUsers(searchKey)
        }
    }

    private fun loadAllUsers() {
        viewModelScope.launch {
            displayLoading()
            when (val result = gitHubUserRepository.getUsers()) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            users = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                else -> handleError(result)
            }
        }
    }

    private fun loadSearchedUsers(searchKey: String = "") {
        viewModelScope.launch {
            displayLoading()
            when (val result = gitHubUserRepository.getSearchUsers(searchKey)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            users = result.data.items ?: emptyList(),
                            isLoading = false,
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