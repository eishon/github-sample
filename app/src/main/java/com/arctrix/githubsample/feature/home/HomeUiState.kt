package com.arctrix.githubsample.feature.home

import com.arctrix.githubsample.data.model.github.User

data class HomeUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)