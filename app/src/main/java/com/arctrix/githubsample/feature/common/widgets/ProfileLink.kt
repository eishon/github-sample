package com.arctrix.githubsample.feature.common.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProfileLink(profileUrl: String, onClick: (String) -> Unit) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = profileUrl)
        withStyle(
            style = SpanStyle(
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(profileUrl)
        }
        pop()
    }
    BasicText(
        text = annotatedString,
        modifier = Modifier.clickable {
            val encodedUrl = URLEncoder.encode(
                profileUrl,
                StandardCharsets.UTF_8.toString()
            )
            onClick(encodedUrl)
        }
    )
}