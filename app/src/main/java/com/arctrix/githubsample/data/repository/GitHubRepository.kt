package com.arctrix.githubsample.data.repository

import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.User

interface GitHubRepository {

    suspend fun getUsers(): ApiResult<List<User>>

    suspend fun getSearchUsers(query: String):ApiResult<SearchUserResponse>
}