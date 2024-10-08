package com.arctrix.githubsample.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arctrix.githubsample.feature.common.theme.GithubSampleTheme
import com.arctrix.githubsample.feature.user_details.UserDetailsScreen
import com.arctrix.githubsample.feature.user_list.UserListScreen
import com.arctrix.githubsample.feature.user_list.UserListViewModel

@Composable
fun HomeScreen(viewModel: UserListViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users") {
        composable("users") {
            UserListScreen(navController, viewModel)
        }
        composable(
            "details/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserDetailsScreen(navController, userId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    GithubSampleTheme {
        HomeScreen()
    }
}