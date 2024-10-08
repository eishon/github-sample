package com.arctrix.githubsample.feature.user_list

import com.arctrix.githubsample.data.model.github.User

data class UserListUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)