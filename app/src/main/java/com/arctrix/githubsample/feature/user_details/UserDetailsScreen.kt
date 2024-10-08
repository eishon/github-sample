package com.arctrix.githubsample.feature.user_details

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arctrix.githubsample.data.model.github.UserDetail
import com.arctrix.githubsample.feature.common.theme.GithubSampleTheme
import com.arctrix.githubsample.feature.common.widgets.BaseAppBar
import com.arctrix.githubsample.feature.common.widgets.HorizontalSpace
import com.arctrix.githubsample.feature.common.widgets.ProfileImage
import com.arctrix.githubsample.feature.common.widgets.ProfileLink
import com.arctrix.githubsample.feature.common.widgets.VerticalSpace

@Composable
fun UserDetailsScreen(
    navController: NavController,
    userId: String?,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val contentColor = if (isDarkTheme) Color.White else Color.Black

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Call the function once when the screen is first created
    LaunchedEffect(Unit) {
        userId?.let { viewModel.loadUserDetails(it) }
    }

    // Implement the UI to show user details based on userId
    Scaffold(
        topBar = {
            BaseAppBar(title = userId, navController = navController)
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                VerticalSpace()

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error.isNullOrEmpty().not()) {
                    Text(text = "Error: ${uiState.error}")
                } else {
                    uiState.userDetail?.let {
                        UserProfile(
                            userDetail = it,
                            backgroundColor = backgroundColor,
                            contentColor = contentColor,
                            navController = navController
                        )
                        VerticalSpace()
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
                        image = Icons.Filled.Menu,
                        text = it,
                        contentColor = contentColor
                    )
                    VerticalSpace()
                }
                location?.let {
                    TextWithIcon(
                        image = Icons.Filled.LocationOn,
                        text = it,
                        contentColor = contentColor
                    )
                    VerticalSpace()
                }

                ProfileLink(profileUrl = htmlUrl) { profileUrl ->
                    navController.navigate("webview/$login/$profileUrl")
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
                    image = Icons.Filled.Edit,
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
fun TextWithIcon(image: ImageVector, text: String, contentColor: Color) {
    Row(
        modifier = Modifier.width(200.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = image,
            contentDescription = text,
            tint = contentColor
        )
        HorizontalSpace(4)
        InfoText(text = text)
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
fun InfoText(text: String) {
    Text(
        text = text,
        maxLines = 3, // Set the maximum number of lines
        overflow = TextOverflow.Ellipsis, // Handle overflow with ellipsis
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun UserDetailsScreenPreview() {
    val navController = rememberNavController()
    GithubSampleTheme {
        UserDetailsScreen(navController = navController, userId = "arctrix")
    }
}