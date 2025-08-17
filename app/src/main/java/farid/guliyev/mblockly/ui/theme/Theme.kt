package farid.guliyev.mblockly.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    secondary = SecondaryIndigoLight,
    tertiary = AccentEmeraldLight,
    background = NeutralGray900,
    surface = NeutralGray800,
    onPrimary = NeutralGray50,
    onSecondary = NeutralGray50,
    onTertiary = NeutralGray50,
    onBackground = NeutralGray100,
    onSurface = NeutralGray100,
    surfaceVariant = NeutralGray700,
    onSurfaceVariant = NeutralGray300
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryIndigo,
    tertiary = AccentEmerald,
    background = BackgroundPrimary,
    surface = SurfacePrimary,
    onPrimary = NeutralGray50,
    onSecondary = NeutralGray50,
    onTertiary = NeutralGray50,
    onBackground = NeutralGray900,
    onSurface = NeutralGray900,
    surfaceVariant = SurfaceSecondary,
    onSurfaceVariant = NeutralGray700
)

@Composable
fun MBlocklyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}