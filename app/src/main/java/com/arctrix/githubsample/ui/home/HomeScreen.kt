package com.arctrix.githubsample.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.arctrix.githubsample.R
import com.arctrix.githubsample.data.model.github.User
import com.arctrix.githubsample.ui.common.theme.GithubSampleTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            UserListScreen(navController, viewModel)
        }
        composable(
            "details/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            UserDetailsScreen(userId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: HomeViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    // Call the function once when the screen is first created
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = {
                        expanded = false
                        viewModel.loadSearchedUsers(searchText)
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(id = R.string.search_hint)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // implement for search suggestions
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error.isNullOrEmpty().not()) {
            Text(text = "Error: ${uiState.error}")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics {
                    traversalIndex = 1f
                },
            ) {
                val list = uiState.users
                items(count = list.size) {
                    UserListItemStateless(user = list[it], backgroundColor) { userId ->
                        navController.navigate("details/$userId")
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetailsScreen(userId: Int?) {
    // Implement the UI to show user details based on userId
    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(text = "User ID: $userId")
            }
        }
    )
}

@Composable
fun UserListItemStateless(user: User, backgroundColor: Color, onClick: (Int) -> Unit) {
    ListItem(
        headlineContent = {
            Text(user.login)
        },
        supportingContent = { Text("Additional info") },
        leadingContent = {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                    .border(
                        2.dp,
                        Color.Gray,
                        RoundedCornerShape(16.dp)
                    ) // Apply border with rounded corners
            )
        },
        colors = ListItemDefaults.colors(containerColor = backgroundColor),
        modifier = Modifier
            .clickable {
                // Navigate to user details screen
                onClick(user.id)
            }
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    GithubSampleTheme {
        HomeScreen()
    }
}