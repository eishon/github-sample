package com.arctrix.githubsample.feature.user_list

import com.arctrix.githubsample.data.model.github.UserItem

data class UserListUiState(
    val users: List<UserItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)