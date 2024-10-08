package com.arctrix.githubsample.ui.home

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
class HomeViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    fun loadAllUsers() {
        viewModelScope.launch {
            displayLoading()
            when (val result = gitHubRepository.getUsers()) {
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

    fun loadSearchedUsers(searchKey: String = "") {
        viewModelScope.launch {
            displayLoading()
            when (val result = gitHubRepository.getSearchUsers(searchKey)) {
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