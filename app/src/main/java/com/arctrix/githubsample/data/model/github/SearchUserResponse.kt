package com.arctrix.githubsample.data.model.github

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SearchUserResponse(
    @Json(name = "total_count")
    val totalCount: Int,
    val items: List<UserItem>
) : Parcelable