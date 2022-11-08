package dev.bogwalk.ui.style

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// number colors
private val MinesweeperBlue = Color(0xff80b5ff)
private val MinesweeperGreen = Color(0xff80ff80)
private val MinesweeperRed = Color(0xffff9580)
private val MinesweeperPurple = Color(0xffe699ff)
private val MinesweeperOrange = Color(0xffffbf80)
private val MinesweeperYellow = Color(0xffffff80)
// digits
private val DigitRedActive = Color(0xffff4d4d)
// greyscale
private val VeryLightGray = Color(0xffbdbdbd)
private val LighterGray = Color(0xff757575)
private val Gray = Color(0xff424242)
private val DarkerGray = Color(0xff212121)

object NumberColors {
    val colors = listOf(
        MinesweeperYellow, MinesweeperBlue, MinesweeperGreen, MinesweeperRed, MinesweeperPurple, MinesweeperOrange
    )
}

val MinesweeperColors = darkColorScheme(
    primary = Gray,
    onPrimary = DarkerGray,
    secondary = LighterGray,
    tertiary = VeryLightGray,
    background = Gray,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xff5d0909),
    onError = DigitRedActive
)

private val MinesweeperTypography = Typography(
    titleSmall = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = .5.sp,
        textAlign = TextAlign.Left
    ),
    labelSmall = TextStyle(
        color = MinesweeperColors.onSurface,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = .5.sp,
        textAlign = TextAlign.Center
    )
)

@Composable
fun MinesweeperTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MinesweeperColors,
        typography = MinesweeperTypography
    ) {
        Surface(content = content)
    }
}