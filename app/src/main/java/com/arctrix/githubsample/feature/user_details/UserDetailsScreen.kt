package com.arctrix.githubsample.feature.user_details

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    navController: NavController,
    userId: String?,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Call the function once when the screen is first created
    LaunchedEffect(Unit) {
        userId?.let { viewModel.loadUserDetails(it) }
    }

    // Implement the UI to show user details based on userId
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$userId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error.isNullOrEmpty().not()) {
                    Text(text = "Error: ${uiState.error}")
                } else {

                    Text(text = "User Name: ${uiState.userDetails?.name}")
                }
            }
        }
    )
}