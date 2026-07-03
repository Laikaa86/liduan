package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  lightColorScheme(
    primary = Green600,
    secondary = Green500,
    tertiary = Green700,
    background = Gray50,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = Gray900,
    onSurface = Gray900
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Green600,
    secondary = Green500,
    tertiary = Green700,
    background = Gray50,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = Gray900,
    onSurface = Gray900
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Force false to prevent white text on white/light backgrounds
  dynamicColor: Boolean = false, // Set false to ensure LiduaN green brand colors
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme


  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
