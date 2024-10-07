package com.arctrix.githubsample.data.repository

import com.arctrix.githubsample.data.model.Result
import com.arctrix.githubsample.data.model.github.User
import retrofit2.http.Query

interface GitHubRepository {

    suspend fun getUsers(): Result<List<User>>

    suspend fun getSearchUsers(query: String):Result<List<User>>
}