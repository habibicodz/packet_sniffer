package com.android.packetsniffer.ui.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF49D782),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1C1C1C),
    onPrimaryContainer = Color.White,
    secondary = Color.Blue,
    tertiary = Color.Blue,
    background = Color(0xFF171717), // Background Color
    onBackground = Color(0xFFE8E8E8),
    surface = Color(0xFF171717), // Actionbar color
    onSurface = Color.White,
    surfaceContainer = Color(0xFF3F3F3F),
    errorContainer = Color(0xFFFF7C7C)


//    surfaceVariant = Color.Black,
//    inversePrimary = Color.Black,
//    inverseSurface = Color.Black,
//    inverseOnSurface = Color.Black,
//    outlineVariant = Color.Black,
//    onSurfaceVariant = Color.Black,
//    secondaryContainer = Color.Black,
//    onSecondary = Color.Black,
//    tertiaryContainer = Color.Black,
//    onTertiary = Color.Black,
//    onTertiaryContainer = Color.Black,
//    onPrimaryContainer = Color.Black,
//    surfaceDim = Color.Black,
//    surfaceTint = Color.Black,
//    surfaceBright = Color.Black,
//    surfaceContainerLow = Color.Black,
//    surfaceContainerHigh = Color.Black,
//    surfaceContainerLowest = Color.Black,
//    surfaceContainerHighest = Color.Black

)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3BBD76),
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color(0xFF131313),
    secondary = Color.Blue,
    tertiary = Color.Blue,
    background = Color(0xFFF1F1F1), // Background Color
    onBackground = Color(0xFF131313),
    surface = Color(0xFFF1F1F1), // Actionbar color
    surfaceContainer = Color(0xFFD7D7D7),
    errorContainer = Color(0xFFFF7C7C)




    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PacketSnifferTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    LocalActivity.current
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    DisposableEffect(Unit) {
        onDispose {

        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}