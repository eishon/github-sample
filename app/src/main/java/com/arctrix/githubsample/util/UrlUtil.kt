package com.arctrix.githubsample.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object UrlUtil {

    fun encodeUrl(url: String): String =
        URLEncoder.encode(
            url,
            StandardCharsets.UTF_8.toString()
        )
}