package com.arctrix.githubsample.data.model.github

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserItem(
    val id: Int = 0,
    val login: String = "",
    @Json(name = "avatar_url")
    val avatarUrl: String = "",
    @Json(name = "html_url")
    val htmlUrl: String = "",
) : Parcelable