package com.arctrix.githubsample.feature.user_list

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.arctrix.githubsample.R
import com.arctrix.githubsample.data.model.github.UserItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: UserListViewModel = hiltViewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by rememberSaveable { mutableStateOf("") }

    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 16.dp,
        label = ""
    )

    // Call the function once when the screen is first created
    LaunchedEffect(Unit) {
        viewModel.loadUsers(searchText)
    }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = searchBarPadding)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = { searchText = it },
                        onSearch = {
                            expanded = false
                            viewModel.loadUsers(searchText)
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
                } else {
                    UserListStateLess(
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
fun UserListStateLess(
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
            UserListItemStateless(
                userItem = users[it],
                inverted = it % 2 == 0,
                backgroundColor = backgroundColor,
                onClick = { userId ->
                    navController.navigate("details/$userId")
                },
                onProfileUrlClick = { profileUrl ->
                    navController.navigate("webview/$profileUrl")
                }
            )
        }
    }
}

@Composable
fun UserListItemStateless(
    userItem: UserItem,
    backgroundColor: Color,
    inverted: Boolean = false,
    onProfileUrlClick: (String) -> Unit,
    onClick: (String) -> Unit
) {
    Card {
        ListItem(
            headlineContent = {
                Text(userItem.login)
            },
            supportingContent = {
                val annotatedString = buildAnnotatedString {
                    pushStringAnnotation(tag = "URL", annotation = userItem.htmlUrl)
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(userItem.htmlUrl)
                    }
                    pop()
                }
                BasicText(
                    text = annotatedString,
                    modifier = Modifier.clickable {
                        val encodedUrl = URLEncoder.encode(
                            userItem.htmlUrl,
                            StandardCharsets.UTF_8.toString()
                        )
                        onProfileUrlClick(encodedUrl)
                    }
                )
            },
            leadingContent = {
                if (inverted.not()) AsyncImage(
                    model = userItem.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)) // Apply rounded corners
                        .border(
                            2.dp,
                            Color.Gray,
                            RoundedCornerShape(8.dp)
                        ) // Apply border with rounded corners
                )
            }, trailingContent = {
                if (inverted) AsyncImage(
                    model = userItem.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)) // Apply rounded corners
                        .border(
                            2.dp,
                            Color.Gray,
                            RoundedCornerShape(8.dp)
                        ) // Apply border with rounded corners
                )
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