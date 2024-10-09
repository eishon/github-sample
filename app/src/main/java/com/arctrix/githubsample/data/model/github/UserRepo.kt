package com.arctrix.githubsample.data.model.github

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserRepo(
    val name: String,
    val description: String?,
    @Json(name = "html_url")
    val htmlUrl: String,
    val language: String?,
    @Json(name = "stargazers_count")
    val stargazersCount: Int,
    @Json(name = "watchers_count")
    val watchersCount: Int,
) : Parcelable