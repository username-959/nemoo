package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val LightColor = lightColorScheme(
    primary = Color(0xFF0B3A66), // Deep Navy
    onPrimary = Color(0xFFFFFFFF), // White
    primaryContainer = Color(0xFFD6EBFF), // Pale Ice Blue
    onPrimaryContainer = Color(0xFF001D36), // Midnight
    secondary = Color(0xFF1FA2FF), // Electric Blue
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFD3EBFF), // Pale Blue
    onSecondaryContainer = Color(0xFF00263F), // Dark Navy
    tertiary = Color(0xFF009966), // Green
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFA0F2D0), // Light Green
    onTertiaryContainer = Color(0xFF00201A), // Dark Teal
    error = Color(0xFFBA1A1A), // Red
    errorContainer = Color(0xFFFFDAD6), // Light Red
    onError = Color(0xFFFFFFFF), // White
    onErrorContainer = Color(0xFF410002), // Dark Red
    background = Color(0xFFF5F9FF), // Ice White
    onBackground = Color(0xFF0B1424), // Near Black Navy
    surface = Color(0xFFF5F9FF), // Ice White
    onSurface = Color(0xFF0B1424), // Near Black Navy
    surfaceVariant = Color(0xFFDCE6F3), // Light Blue Gray
    onSurfaceVariant = Color(0xFF3F4F66), // Slate
    outline = Color(0xFF70829C), // Slate Blue
    outlineVariant = Color(0xFFC3D0E2), // Light Slate
    inverseSurface = Color(0xFF14233B), // Navy
    inverseOnSurface = Color(0xFFE8F0FA), // Ice
    inversePrimary = Color(0xFF7CC4FF), // Ice Blue
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFF0B3A66), // Deep Navy
    surfaceContainerLowest = Color(0xFFFFFFFF), // White
    surfaceContainerLow = Color(0xFFEFF5FD), // Very Light Blue
    surfaceContainer = Color(0xFFE8F0FA), // Light Blue
    surfaceContainerHigh = Color(0xFFE0EAF6), // Light Blue
    surfaceContainerHighest = Color(0xFFD8E4F2), // Light Blue
)

private val DarkColor = darkColorScheme(
    primary = Color(0xFF7CC4FF), // Ice Blue
    onPrimary = Color(0xFF00263F), // Dark Navy
    primaryContainer = Color(0xFF0F3A5F), // Deep Blue
    onPrimaryContainer = Color(0xFFCDE8FF), // Pale Ice
    secondary = Color(0xFF38B6FF), // Electric Blue (main accent)
    onSecondary = Color(0xFF00263F), // Dark Navy
    secondaryContainer = Color(0xFF123A5E), // Deep Blue
    onSecondaryContainer = Color(0xFFD3EBFF), // Pale Blue
    tertiary = Color(0xFF83D6B5), // Mint Green
    onTertiary = Color(0xFF00382E), // Dark Teal
    tertiaryContainer = Color(0xFF005143), // Teal
    onTertiaryContainer = Color(0xFFA0F2D0), // Light Green
    error = Color(0xFFFFB4AB), // Light Red
    errorContainer = Color(0xFF93000A), // Dark Red
    onError = Color(0xFF690005), // Deep Red
    onErrorContainer = Color(0xFFFFDAD6), // Light Red
    background = Color(0xFF050A14), // Deep Navy Black
    onBackground = Color(0xFFE8F0FA), // Ice White
    surface = Color(0xFF0A1220), // Navy Black
    onSurface = Color(0xFFE8F0FA), // Ice White
    surfaceVariant = Color(0xFF1B2B45), // Navy
    onSurfaceVariant = Color(0xFF9FB3CC), // Muted Ice
    outline = Color(0xFF4A6A94), // Steel Blue
    outlineVariant = Color(0xFF1F3454), // Dark Steel
    inverseSurface = Color(0xFFE8F0FA), // Ice White
    inverseOnSurface = Color(0xFF0A1220), // Navy Black
    inversePrimary = Color(0xFF0B3A66), // Deep Navy
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFF7CC4FF), // Ice Blue
    surfaceContainerLowest = Color(0xFF030710), // Near Black
    surfaceContainerLow = Color(0xFF0A1424), // Navy
    surfaceContainer = Color(0xFF0E1A2E), // Navy
    surfaceContainerHigh = Color(0xFF13223A), // Navy
    surfaceContainerHighest = Color(0xFF192B47), // Navy
)

// Semantic Colors
val colorPing = Color(0xFF2ED47A) // Green
val colorPingRed = Color(0xFFFF4D6D) // Pink Red
val colorConfigType = Color(0xFF5CB8FF) // Ice Blue
val colorFabActive = Color(0xFF38B6FF) // Electric Blue
val colorFabInactiveLight = Color(0xFF9AA8BC) // Gray Blue
val colorFabInactiveDark = Color(0xFF4A5A70) // Dark Gray Blue
val dividerColorLight = Color(0xFFD5DFEC) // Light Blue Gray
val dividerColorDark = Color(0xFF1B2B45) // Navy

// Glass look: translucent fill + thin light edge
val glassFillDark = Color(0x14FFFFFF) // White 8%
val glassFillLight = Color(0xB3FFFFFF) // White 70%
val glassBorderTopDark = Color(0x4DFFFFFF) // White 30%
val glassBorderBottomDark = Color(0x14FFFFFF) // White 8%
val glassBorderTopLight = Color(0x330B3A66) // Navy 20%
val glassBorderBottomLight = Color(0x140B3A66) // Navy 8%

// App background gradient (top to bottom)
val backgroundGradientDark = listOf(
    Color(0xFF0B1A33),
    Color(0xFF060D1B),
    Color(0xFF02050C)
)
val backgroundGradientLight = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFF5F9FF),
    Color(0xFFE8F0FA)
)

// Toast Colors 85%
val toastNormalBgLight = Color(0xD9353A3E) // Dark Gray
val toastNormalBgDark = Color(0xD94A4F54) // Darker Gray
val toastSuccessBg = Color(0xD9388E3C) // Green
val toastErrorBg = Color(0xD9D50000) // Red
val toastInfoBg = Color(0xD93F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    // Defaults: dark theme ("2") and no dynamic system colors, so the navy look is what people see first.
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "2") ?: "2"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "2") ?: "2"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        "2" -> true
        else -> isSystemInDarkTheme()
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor by ThemeManager.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
