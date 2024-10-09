package com.arctrix.githubsample.data.api

import com.arctrix.githubsample.data.model.github.SearchUserResponse
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserItem
import com.arctrix.githubsample.data.model.github.UserRepo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(): List<UserItem>

    @GET("search/users")
    suspend fun getUsersSearch(@Query("q") query: String): SearchUserResponse

    @GET("users/{userId}")
    suspend fun getUsersDetails(@Path("userId") query: String): UserDetail

    @GET("users/{userId}/repos")
    suspend fun getUserRepos(@Path("userId") userId: String): List<UserRepo>
}