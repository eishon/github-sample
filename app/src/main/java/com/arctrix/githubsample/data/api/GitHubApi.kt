package com.arctrix.githubsample.data.api

import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.User
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("search/users")
    suspend fun getUsersSearch(@Query("q") query: String): SearchUserResponse
}