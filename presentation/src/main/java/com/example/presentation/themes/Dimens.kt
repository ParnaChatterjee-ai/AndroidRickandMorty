package com.example.presentation.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Define custom dimensions here
object Dimens {
    val smallPadding: Dp = 8.dp
    val mediumPadding: Dp = 16.dp
    val largePadding: Dp = 32.dp
    val smallMargin: Dp = 8.dp
    val mediumMargin: Dp = 16.dp
    val largeMargin: Dp = 32.dp
    val app_bar_height: Dp = 100.dp
    val textSizeSmall: Dp = 12.dp
    val textSizeMedium: Dp = 14.dp
    val textSizeLarge:  Dp= 18.dp
    val cardWidth = 130.dp
    val cardHeight = 130.dp
    val elevation = 12.dp
}

val LocalDimens = compositionLocalOf{ Dimens }
val MaterialTheme.dimens:Dimens
    @Composable
    get() = LocalDimens.current
