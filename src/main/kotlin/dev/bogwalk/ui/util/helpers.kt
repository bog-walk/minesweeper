package dev.bogwalk.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bogwalk.ui.style.*

/**
 * Draws a border that resembles either an elevated or sunken bevelled edge.
 */
fun ContentDrawScope.drawBevelEdge(strokeWidth: Float, isElevated: Boolean = true) {
    if (isElevated) {
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(size.width - ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
    } else {
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(size.width - SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
    }
}

fun DrawScope.drawBevelEdge(strokeWidth: Float, isElevated: Boolean = true) {
    if (isElevated) {
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(size.width - ELEVATED_BEVEL, ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            Offset(size.width - ELEVATED_BEVEL, size.height - ELEVATED_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
    } else {
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(size.width - SUNKEN_BEVEL, SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.secondaryVariant,
            Offset(SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            Offset(size.width - SUNKEN_BEVEL, size.height - SUNKEN_BEVEL),
            strokeWidth,
            StrokeCap.Square
        )
    }
}

enum class GameLevel(val values: List<Int>, val size: Pair<Dp, Dp>) {
    BEGINNER(listOf(9, 9, 10), 226.dp to 337.dp),
    INTERMEDIATE(listOf(16, 16, 40), 366.dp to 477.dp),
    EXPERT(listOf(16, 30, 99), 646.dp to 477.dp);

    override fun toString(): String {
        return "${name.padEnd(15)} ${values[0]} by ${values[1]}   ${values[2]} mines"
    }
}