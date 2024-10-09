package com.arctrix.githubsample.feature.user_details

import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserRepo

data class UserDetailsUiState(
    val userDetail: UserDetail? = null,
    val userRepos: List<UserRepo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)