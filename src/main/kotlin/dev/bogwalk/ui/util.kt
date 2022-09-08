package dev.bogwalk.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import dev.bogwalk.ui.style.ELEVATED_BEVEL
import dev.bogwalk.ui.style.MinesweeperColors
import dev.bogwalk.ui.style.SUNKEN_BEVEL

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