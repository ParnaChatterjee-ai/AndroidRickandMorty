package com.example.presentation.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

object Colors {
 val purple_200 = Color(color =0xFFBB86FC)
 val purple_500 = Color(color =0xFF6200EE)
 val purple_700 = Color(color =0xFF3700B3)
 val teal_200 = Color(color =0xFF03DAC5)
 val teal_700 = Color(color =0xFF018786)
 val black = Color(color =0xFF000000)
 val white = Color(color =0xFFFFFFFF)
 val topbar_box = Color(color =0xFFFEF7EA)
 val episode_start = Color(color = 0xFF3700B3)
 val episode_end = Color(color = 0xFF625B71)
}

val LocalColor = compositionLocalOf{ Colors }
val MaterialTheme.color:Colors
 @Composable
 get() = LocalColor.current
