package com.arctrix.githubsample.feature.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arctrix.githubsample.feature.user_details.UserDetailsScreen
import com.arctrix.githubsample.feature.user_list.UserListScreen
import com.arctrix.githubsample.feature.webview.WebViewScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users") {
        composable("users") {
            UserListScreen(navController)
        }
        composable(
            "details/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserDetailsScreen(navController, userId)
        }
        composable(
            "webview/{userId}/{profileUrl}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("profileUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val profileUrl = backStackEntry.arguments?.getString("profileUrl")
            WebViewScreen(navController, userId, profileUrl)
        }
    }
}
