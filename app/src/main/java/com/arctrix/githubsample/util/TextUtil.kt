package com.arctrix.githubsample.util

object TextUtil {

    fun sanitizeSearchInput(input: String?): String {
        return (input ?: "").trim() // Remove leading/trailing whitespace
            .replace(Regex("[^A-Za-z0-9-]"), "") // Remove non-alphanumeric characters and hyphens
            .replace(Regex("-{2,}"), "-") // Replace multiple hyphens with a single hyphen
            .removePrefix("-") // Remove leading hyphen
            .removeSuffix("-") // Remove trailing hyphen
            .take(39) // GitHub usernames have a max length of 39 characters
    }
}