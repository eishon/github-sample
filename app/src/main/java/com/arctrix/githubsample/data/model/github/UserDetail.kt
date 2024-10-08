package com.arctrix.githubsample.data.model.github

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserDetail(
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "repos_url")
    val reposUrl: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val bio: String?,
    @Json(name = "public_repos")
    val publicRepos: Int,
    val followers: Int,
    val following: Int
) : Parcelable