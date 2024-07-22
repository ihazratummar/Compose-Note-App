package com.example.mynotes.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

data class WindowSize(
    val width: WindowType,
    val height: WindowType
)

enum class WindowType {
    Compact, Medium, Expanded
}

@Composable
fun rememberWindowSize(): WindowSize {
    val config = LocalConfiguration.current

    return WindowSize(
        width = when{
            config.screenWidthDp < 600 -> WindowType.Compact
            config.screenWidthDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        },
        height = when{
            config.screenHeightDp < 480 -> WindowType.Compact
            config.screenHeightDp < 900 -> WindowType.Medium
            else -> WindowType.Expanded
        }
    )
}
