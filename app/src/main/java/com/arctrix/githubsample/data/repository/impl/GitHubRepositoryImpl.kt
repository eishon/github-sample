package com.arctrix.githubsample.data.repository.impl

import com.arctrix.githubsample.data.api.GitHubApi
import com.arctrix.githubsample.data.model.ApiResult
import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.User
import com.arctrix.githubsample.data.repository.GitHubRepository
import com.arctrix.githubsample.data.repository.helper.RepositoryHelper

class GitHubRepositoryImpl(private val gitHubApi: GitHubApi) : GitHubRepository {

    override suspend fun getUsers(): ApiResult<List<User>> = RepositoryHelper.execute {
        gitHubApi.getUsers()
    }

    override suspend fun getSearchUsers(query: String): ApiResult<SearchUserResponse>  =
        RepositoryHelper.execute {
            gitHubApi.getUsersSearch(query)
        }
}