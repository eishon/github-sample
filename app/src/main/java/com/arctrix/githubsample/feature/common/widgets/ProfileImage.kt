package com.arctrix.githubsample.feature.common.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileImage(imageUrl: String, size : Int = 64) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp)) // Apply rounded corners
            .border(
                2.dp,
                Color.Gray,
                RoundedCornerShape(8.dp)
            ) // Apply border with rounded corners
    )
}