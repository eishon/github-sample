package com.arctrix.githubsample.feature.user_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arctrix.githubsample.R
import com.arctrix.githubsample.data.model.github.UserItem
import com.arctrix.githubsample.feature.common.theme.GithubSampleTheme
import com.arctrix.githubsample.feature.common.widgets.ProfileImage
import com.arctrix.githubsample.feature.common.widgets.ProfileLink
import com.arctrix.githubsample.util.RouteUtil
import com.arctrix.githubsample.util.TextUtil
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(navController: NavController, viewModel: UserListViewModel = hiltViewModel()) {
    val backgroundColor = colorResource(id = R.color.background)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val message = stringResource(id = R.string.input_sanitized)
    val label = stringResource(id = R.string.dismiss)

    // Call the function once when the screen is first created
    LaunchedEffect(true) {
        if (uiState.users.isEmpty()) {
            viewModel.loadUsers(searchText)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        topBar = {
            UserSearchBar(
                searchText = searchText,
                onSearch = {
                    TextUtil.sanitizeSearchInput(input = searchText).let { sanitizedText ->
                        viewModel.loadUsers(sanitizedText)
                        if (searchText != sanitizedText) {
                            if (searchText.trim() != sanitizedText.trim()) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = message,
                                        actionLabel = label,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                            searchText = sanitizedText
                        }
                    }
                },
                onQueryChange = { searchText = it }
            )
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .semantics { isTraversalGroup = true }
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error.isNullOrEmpty().not()) {
                    Text(text = "Error: ${uiState.error}")
                } else if (uiState.users.isEmpty()) {
                    Text(text = stringResource(id = R.string.no_user_found))
                } else {
                    UserList(
                        users = uiState.users,
                        navController = navController,
                        backgroundColor = backgroundColor
                    )
                }
            }
        }
    )
}

@Composable
fun UserList(
    users: List<UserItem>,
    navController: NavController,
    backgroundColor: Color
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics {
            traversalIndex = 1f
        },
    ) {
        items(count = users.size) {
            UserListItem(
                userItem = users[it],
                inverted = it % 2 == 0,
                backgroundColor = backgroundColor,
                onClick = { userId ->
                    navController.navigate(RouteUtil.createUserDetailsRoute(userId))
                },
                onProfileUrlClick = { userId, profileUrl ->
                    navController.navigate(RouteUtil.createWebViewRoute(userId, profileUrl))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchBar(
    searchText: String,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 16.dp,
        label = ""
    )

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = searchBarPadding)
            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText,
                onQueryChange = onQueryChange,
                onSearch = {
                    expanded = false
                    onSearch(it)
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
}

@Composable
fun UserListItem(
    userItem: UserItem,
    backgroundColor: Color,
    inverted: Boolean = false,
    onProfileUrlClick: (String, String) -> Unit,
    onClick: (String) -> Unit
) {
    Card {
        ListItem(
            headlineContent = {
                Text(userItem.login)
            },
            supportingContent = {
                ProfileLink(profileUrl = userItem.htmlUrl) { profileUrl ->
                    onProfileUrlClick(userItem.login, profileUrl)
                }
            },
            leadingContent = {
                if (inverted.not()) ProfileImage(imageUrl = userItem.avatarUrl)
            },
            trailingContent = {
                if (inverted) ProfileImage(imageUrl = userItem.avatarUrl)
            },
            colors = ListItemDefaults.colors(containerColor = backgroundColor),
            modifier = Modifier
                .clickable {
                    // Navigate to user details screen
                    onClick(userItem.login)
                }
                .fillMaxWidth()
                .height(96.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserSearchPreview() {
    GithubSampleTheme {
        UserSearchBar(
            searchText = "",
            onSearch = { },
            onQueryChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    val backgroundColor = colorResource(id = R.color.background)

    val user = UserItem(
        id = 1,
        login = "User1",
        avatarUrl = "https://avatars.githubusercontent.com/u/31154892?v=4",
        htmlUrl = "https://github.com/User1"
    )

    GithubSampleTheme {
        UserListItem(
            userItem = user,
            backgroundColor = backgroundColor,
            onProfileUrlClick = { _, _ -> },
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    val backgroundColor = colorResource(id = R.color.background)
    val navController = rememberNavController()

    val users = listOf(
        UserItem(
            id = 1,
            login = "User1",
            avatarUrl = "https://avatars.githubusercontent.com/u/31154892?v=4",
            htmlUrl = "https://github.com/User1"
        ),
        UserItem(
            id = 2,
            login = "User2",
            avatarUrl = "https://avatars.githubusercontent.com/u/94143866?v=4",
            htmlUrl = "https://github.com/User2"
        ),
        UserItem(
            id = 3,
            login = "User3",
            avatarUrl = "https://avatars.githubusercontent.com/u/18392689?v=4",
            htmlUrl = "https://github.com/User3"
        )
    )

    GithubSampleTheme {
        UserList(
            users = users,
            navController = navController,
            backgroundColor = backgroundColor
        )
    }
}