package com.arctrix.githubsample.feature.user_details

import com.arctrix.githubsample.data.model.github.UserDetail

data class UserDetailsUiState(
    val userDetails: UserDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)