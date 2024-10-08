package com.arctrix.githubsample.feature.common.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpace(size: Int = 8) {
    Spacer(modifier = Modifier.height(size.dp))
}

@Composable
fun HorizontalSpace(size: Int = 8) {
    Spacer(modifier = Modifier.width(size.dp))
}