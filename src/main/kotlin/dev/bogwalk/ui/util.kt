package dev.bogwalk.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

/**
 * Draws a border that resembles either an elevated or sunken bevelled edge.
 */
fun ContentDrawScope.drawEdge(strokeWidth: Float, isElevated: Boolean = true) {
    if (isElevated) {
        drawLine(Color.White, Offset.Zero, Offset(size.width, 0F), strokeWidth)
        drawLine(Color.White, Offset.Zero, Offset(0F, size.height), strokeWidth)
        drawLine(Color.Gray, Offset(size.width, 0F), Offset(size.width, size.height), strokeWidth)
        drawLine(Color.Gray, Offset(0F, size.height), Offset(size.width, size.height), strokeWidth)
    } else {
        drawLine(Color.Gray, Offset.Zero, Offset(size.width, 0F), strokeWidth)
        drawLine(Color.Gray, Offset.Zero, Offset(0F, size.height), strokeWidth)
        drawLine(Color.White, Offset(size.width, 0F), Offset(size.width, size.height), strokeWidth)
        drawLine(Color.White, Offset(0F, size.height), Offset(size.width, size.height), strokeWidth)
    }
}