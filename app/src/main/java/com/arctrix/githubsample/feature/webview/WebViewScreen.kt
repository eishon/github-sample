package com.arctrix.githubsample.feature.webview

import android.content.res.Configuration
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arctrix.githubsample.feature.common.theme.GithubSampleTheme
import com.arctrix.githubsample.feature.common.widgets.BaseAppBar

@Composable
fun WebViewScreen(navController: NavController, title: String?, url: String?) {

    Scaffold(
        topBar = {
            BaseAppBar(title = title, navController = navController)
        },
        content = { innerPadding ->
            if (url.isNullOrEmpty()) {

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    text = "Invalid URL"
                )
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            loadUrl(url)
                        }
                    }
                )
            }
        }
    )
}

@Preview(backgroundColor = 0xFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun UserProfilePreview() {
    val navController = rememberNavController()
    GithubSampleTheme {
        WebViewScreen(
            navController = navController,
            title = "WebView",
            url = "https://www.google.com"
        )
    }
}
