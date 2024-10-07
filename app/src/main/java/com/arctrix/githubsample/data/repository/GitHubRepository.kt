package com.arctrix.githubsample.data.repository

interface GitHubRepository {

    fun getUsers(): List<Any>

    fun getSearchUsers(query: String): List<Any>
}