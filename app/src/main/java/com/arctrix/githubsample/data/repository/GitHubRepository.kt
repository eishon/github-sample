package com.arctrix.githubsample.data.repository

import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserItem

interface GitHubRepository {

    suspend fun getUsers(): ApiResult<List<UserItem>>

    suspend fun getSearchUsers(query: String):ApiResult<SearchUserResponse>

    suspend fun getUserDetails(userId: String): ApiResult<UserDetail>
}