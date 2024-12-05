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
        val elevatedBevel = 1f
        drawLine(
            MinesweeperColors.tertiary,
            Offset(elevatedBevel, elevatedBevel),
            Offset(size.width - elevatedBevel, elevatedBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.tertiary,
            Offset(elevatedBevel, elevatedBevel),
            Offset(elevatedBevel, size.height - elevatedBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(size.width - elevatedBevel, elevatedBevel),
            Offset(size.width - elevatedBevel, size.height - elevatedBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(elevatedBevel, size.height - elevatedBevel),
            Offset(size.width - elevatedBevel, size.height - elevatedBevel),
            strokeWidth,
            StrokeCap.Square
        )
    } else {
        val sunkenBevel = -2f
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(sunkenBevel, sunkenBevel),
            Offset(size.width - sunkenBevel, sunkenBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset(sunkenBevel, sunkenBevel),
            Offset(sunkenBevel, size.height - sunkenBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.tertiary,
            Offset(size.width - sunkenBevel, sunkenBevel),
            Offset(size.width - sunkenBevel, size.height - sunkenBevel),
            strokeWidth,
            StrokeCap.Square
        )
        drawLine(
            MinesweeperColors.tertiary,
            Offset(sunkenBevel, size.height - sunkenBevel),
            Offset(size.width - sunkenBevel, size.height - sunkenBevel),
            strokeWidth,
            StrokeCap.Square
        )
    }
}