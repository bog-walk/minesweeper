package dev.bogwalk.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.bogwalk.ui.style.*

/**
 * Draws a border that resembles either an elevated or sunken bevelled edge.
 */
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