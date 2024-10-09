package com.arctrix.githubsample.feature.user_details

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arctrix.githubsample.R
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.data.model.github.UserRepo
import com.arctrix.githubsample.feature.common.theme.GithubSampleTheme
import com.arctrix.githubsample.feature.common.widgets.BaseAppBar
import com.arctrix.githubsample.feature.common.widgets.HorizontalSpace
import com.arctrix.githubsample.feature.common.widgets.ProfileImage
import com.arctrix.githubsample.feature.common.widgets.ProfileLink
import com.arctrix.githubsample.feature.common.widgets.VerticalSpace
import com.arctrix.githubsample.util.UrlUtil

@Composable
fun UserDetailsScreen(
    navController: NavController,
    userId: String?,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    val backgroundColor = colorResource(id = R.color.background)
    val contentColor = colorResource(id = R.color.content)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Call the function once when the screen is first created
    LaunchedEffect(true) {
        userId?.let {
            if (uiState.userDetail == null) {
                viewModel.loadUserDetails(it)
            }
        }
    }

    // Implement the UI to show user details based on userId
    Scaffold(
        topBar = {
            BaseAppBar(title = userId, navController = navController)
        },
        content = { innerPadding ->
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item { VerticalSpace() }

                if (uiState.isLoading) {
                    item { CircularProgressIndicator() }
                } else if (uiState.error.isNullOrEmpty().not()) {
                    item { Text(text = "Error: ${uiState.error}") }
                } else {
                    uiState.apply {
                        userDetail?.let {
                            item {
                                UserProfile(
                                    userDetail = it,
                                    backgroundColor = backgroundColor,
                                    contentColor = contentColor,
                                    navController = navController
                                )
                            }
                            item { VerticalSpace() }
                            item {
                                UserRepos(
                                    userRepos = userRepos,
                                    backgroundColor = backgroundColor,
                                    contentColor = contentColor,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserProfile(
    userDetail: UserDetail,
    backgroundColor: Color,
    contentColor: Color,
    navController: NavController
) {
    userDetail.apply {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                VerticalSpace(48)
                ProfileImage(imageUrl = avatarUrl, size = 256)
                VerticalSpace()
                name?.let {
                    Text(text = it)
                    VerticalSpace()
                }
                Text(text = "@${login}")
                VerticalSpace()
                company?.let {
                    TextWithIcon(
                        painterResource(id = R.drawable.ic_workplace),
                        text = it,
                        contentColor = contentColor
                    )
                    VerticalSpace()
                }
                location?.let {
                    TextWithIcon(
                        painter = painterResource(id = R.drawable.ic_location),
                        text = it,
                        contentColor = contentColor
                    )
                    VerticalSpace()
                }

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalSpace(8)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = "profile",
                        tint = contentColor
                    )
                    HorizontalSpace(16)
                    ProfileLink(profileUrl = htmlUrl) { profileUrl ->
                        navController.navigate("webview/$login/$profileUrl")
                    }
                    HorizontalSpace(8)
                }

                VerticalSpace()

                VerticalSpace(24)
            }
        }

        VerticalSpace(16)

        bio?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                TextWithIcon(
                    painterResource(id = R.drawable.ic_bio),
                    text = it,
                    contentColor = contentColor
                )
                VerticalSpace()
            }
            VerticalSpace(16)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextWithCount(
                title = "Follower",
                value = "$followers",
                backgroundColor = backgroundColor
            )
            TextWithCount(
                title = "Repos",
                value = "$publicRepos",
                backgroundColor = backgroundColor
            )
            TextWithCount(
                title = "Following",
                value = "$following",
                backgroundColor = backgroundColor
            )
        }
    }
}

@Composable
fun UserRepos(
    userRepos: List<UserRepo>,
    backgroundColor: Color,
    contentColor: Color,
    navController: NavController
) {
    userRepos.apply {
        ReposList(
            repos = userRepos,
            navController = navController,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}

@Composable
fun TextWithIcon(painter: Painter, text: String, contentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalSpace(8)
        Icon(
            painter = painter,
            contentDescription = text,
            tint = contentColor
        )
        HorizontalSpace(4)
        Text(
            text = text,
            maxLines = 5, // Set the maximum number of lines
            overflow = TextOverflow.Ellipsis, // Handle overflow with ellipsis
            modifier = Modifier.padding(8.dp)
        )
        HorizontalSpace(8)
    }
}

@Composable
fun TextWithCount(title: String, value: String, backgroundColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title, textAlign = TextAlign.Center)
            VerticalSpace(4)
            Text(text = value, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ReposList(
    repos: List<UserRepo>,
    navController: NavController,
    backgroundColor: Color,
    contentColor: Color
) {
    repeat(repos.size) {
        RepoListItem(
            repoItem = repos[it],
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            onClick = { url ->
                navController.navigate("webview/${repos[it].name}/$url")
            }
        )
    }
}

@Composable
fun RepoListItem(
    repoItem: UserRepo,
    backgroundColor: Color,
    contentColor: Color,
    onClick: (String) -> Unit
) {
    repoItem.apply {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            ListItem(
                overlineContent = {
                    language?.let {
                        Text(text = "in $it")
                    }
                },
                headlineContent = {
                    Text(name)
                },
                supportingContent = {
                    description?.let {
                        Text(
                            text = it,
                            maxLines = 2, // Set the maximum number of lines
                            overflow = TextOverflow.Ellipsis// Handle overflow with ellipsis)
                        )
                    }
                },
                trailingContent = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Stars",
                                tint = contentColor
                            )
                            VerticalSpace(4)
                            Text(text = "$stargazersCount")
                        }
                        HorizontalSpace(8)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_watching),
                                contentDescription = "Stars",
                                tint = contentColor
                            )
                            VerticalSpace(4)
                            Text(text = "$watchersCount")
                        }
                    }
                },
                colors = ListItemDefaults.colors(containerColor = backgroundColor),
                modifier = Modifier
                    .clickable {
                        onClick(UrlUtil.encodeUrl(htmlUrl))
                    }
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun UserProfilePreview() {
    val backgroundColor = colorResource(id = R.color.background)
    val contentColor = colorResource(id = R.color.content)
    val navController = rememberNavController()

    val userDetail = UserDetail(
        login = "User1",
        avatarUrl = "https://avatars.githubusercontent.com/u/31154892?v=4",
        htmlUrl = "https://github.com/User1",
        reposUrl = "https://api.github.com/users/User1/repos",
        name = "lazy_potato_0_0_",
        company = "GitHub",
        location = "Tokyo, Japan",
        bio = "Mobile Application Developer (Android, iOS, Flutter, Unity)",
        publicRepos = 29,
        followers = 5,
        following = 3
    )

    GithubSampleTheme {
        LazyColumn {
            item {
                UserProfile(
                    userDetail = userDetail,
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    navController = navController
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun UserReposPreview() {
    val backgroundColor = colorResource(id = R.color.background)
    val contentColor = colorResource(id = R.color.content)
    val navController = rememberNavController()

    val userRepos = listOf(
        UserRepo(
            name = "Android-Samples",
            description = "This repository is for android application code samples.",
            htmlUrl = "https://github.com/eishon/Android-Samples",
            stargazersCount = 1,
            watchersCount = 1,
            language = "Kotlin"
        ),
        UserRepo(
            name = "Appium-Automation-Testing",
            description = null,
            htmlUrl = "https://github.com/eishon/Appium-Automation-Testing",
            language = "Java",
            stargazersCount = 1,
            watchersCount = 2
        ),
        UserRepo(
            name = "Arduino-Speed-Measurement",
            description = "An simple Arduino setup for measuring speed of a moving object and Android app for showing the speed.",
            htmlUrl = "https://github.com/eishon/Arduino-Speed-Measurement",
            language = "Java",
            stargazersCount = 1,
            watchersCount = 1
        )
    )

    GithubSampleTheme {
        LazyColumn {
            item {
                UserRepos(
                    userRepos = userRepos,
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    navController = navController
                )
            }
        }
    }
}