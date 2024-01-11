package com.tabarkevych.places_app.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorScheme = lightColorScheme(
    primary = Mirage,
    secondary =  Color.White,
    tertiary =  Salomie,
    background = Salomie,
    surface = Color.White,
    onPrimary = BayLeaf,
    onSecondary = Mirage,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,

)
private val DarkColorScheme = darkColorScheme(
    primary = Salomie,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Mirage,
    surface = Color(0xFF1C1B1F),
    onPrimary = Mirage,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)


@Composable
fun PlacesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
                systemUiController.setStatusBarColor(
                    color = colorScheme.onPrimary,
                    darkIcons = false
                )
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}