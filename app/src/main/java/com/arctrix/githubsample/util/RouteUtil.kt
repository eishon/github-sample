package com.arctrix.githubsample.util

import com.arctrix.githubsample.data.Constants

object RouteUtil {

    fun createUserListRoute() = Constants.ROUTE_USERS

    fun createUserDetailsRoute(userId: String?) = "${Constants.ROUTE_USER_DETAILS}/$userId"

    fun createWebViewRoute(title: String?, url: String?) = "${Constants.ROUTE_WEB_VIEW}/$title/$url"
}