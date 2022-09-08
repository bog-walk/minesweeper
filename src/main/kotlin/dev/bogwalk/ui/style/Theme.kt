package dev.bogwalk.ui.style

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// colors
private val MinesweeperBlue = Color(0xff80b5ff)
private val MinesweeperGreen = Color(0xff80ff80)
private val MinesweeperRed = Color(0xffff9580)
private val MinesweeperYellow = Color(0xffffff80)
// greyscale
private val VeryLightGray = Color(0xffbdbdbd)
private val LighterGray = Color(0xff757575)
private val Gray = Color(0xff424242)
private val DarkerGray = Color(0xff212121)

object NumberColors {
    val colors = listOf(MinesweeperRed, MinesweeperBlue, MinesweeperGreen)
}

val MinesweeperColors = darkColors(
    primary = Gray,
    secondary = LighterGray,
    secondaryVariant = VeryLightGray,
    background = Gray,
    surface = Color.White,
    error = Color(0xff5d0909),
    onPrimary = DarkerGray
)

private val MinesweeperTypography = Typography(
    defaultFontFamily = FontFamily.Monospace,
    h4 = TextStyle(
        fontSize = 37.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,
        lineHeight = 37.sp
    ),
    body1 = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    ),
    button = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
    )
)

@Composable
fun MinesweeperTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = MinesweeperColors,
        typography = MinesweeperTypography
    ) {
        Surface(content = content)
    }
}