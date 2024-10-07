package com.arctrix.githubsample.data.repository.impl

import com.arctrix.githubsample.data.api.GitHubApi
import com.arctrix.githubsample.data.repository.GitHubRepository

class GitHubRepositoryImpl (gitHubApi: GitHubApi) : GitHubRepository {

    override fun getUsers(): List<Any> {
        return emptyList()
    }
    override fun getSearchUsers(query: String): List<Any> {
        return emptyList()
    }
}