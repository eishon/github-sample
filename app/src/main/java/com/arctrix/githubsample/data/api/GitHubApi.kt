package com.arctrix.githubsample.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(): List<Any>

    @GET("search/users")
    suspend fun getUsersSearch(@Query("q") queries: String): Any
}