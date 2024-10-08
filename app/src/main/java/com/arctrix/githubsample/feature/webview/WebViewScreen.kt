package com.arctrix.githubsample.feature.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.arctrix.githubsample.feature.common.widgets.BaseAppBar

@Composable
fun WebViewScreen(navController: NavController, userId: String?, url: String?) {
    Scaffold(
        topBar = {
            BaseAppBar(title = userId, navController = navController)
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