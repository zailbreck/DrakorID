package id.co.drakorid.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF22D3EE),
    onPrimary = Color(0xFF020617),
    primaryContainer = Color(0xFF083344),
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF020617),
    secondaryContainer = Color(0xFF064E3B),
    tertiary = Color(0xFFA78BFA),
    onTertiary = Color(0xFF020617),
    tertiaryContainer = Color(0xFF4C1D95),
    background = Color(0xFF020617),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFFB7185),
    onError = Color(0xFF020617),
    errorContainer = Color(0xFF881337),
)

@Composable
fun DrakorTVTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

object TvColors {
    val primary = Color(0xFF22D3EE)
    val error = Color(0xFFFB7185)
    val focusBorder = Color(0xFF22D3EE)
    val focusBackground = Color(0xFF083344)
    val cardBackground = Color(0xFF1E293B)
    val cardBorder = Color(0xFF334155)
    val textPrimary = Color(0xFFF8FAFC)
    val textSecondary = Color(0xFF94A3B8)
    val textMuted = Color(0xFF64748B)
    val gradientStart = Color(0xFF020617)
    val gradientEnd = Color(0xFF0F172A)
    val ratingStar = Color(0xFFFBBF24)
    val onPrimary = Color(0xFF020617)
}
