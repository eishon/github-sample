package com.arctrix.githubsample.feature.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arctrix.githubsample.data.Constants
import com.arctrix.githubsample.feature.user_details.UserDetailsScreen
import com.arctrix.githubsample.feature.user_list.UserListScreen
import com.arctrix.githubsample.feature.webview.WebViewScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Constants.ROUTE_USERS) {
        composable(Constants.ROUTE_USERS) {
            UserListScreen(navController)
        }
        composable(
            "${Constants.ROUTE_USER_DETAILS}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserDetailsScreen(navController, userId)
        }
        composable(
            "${Constants.ROUTE_WEB_VIEW}/{userId}/{url}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val url = backStackEntry.arguments?.getString("url")
            WebViewScreen(navController, userId, url)
        }
    }
}
