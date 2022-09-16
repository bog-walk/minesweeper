package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import dev.bogwalk.model.DigitSegment
import dev.bogwalk.ui.style.*

@Composable
fun DigitalScreen(
    count: Int
) {
    var remainder = count
    var pow = 100

    Row(
        modifier = Modifier
            .background(MinesweeperColors.onPrimary),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            key("Digit $it") {
                SegmentDigit(DigitSegment.fromDigit(remainder / pow))

                remainder %= pow
                pow /= 10
            }
        }
    }
}

/**
 * Based on the retro 7-segment display clock design.
 */
@Composable
private fun SegmentDigit(
    segment: DigitSegment
) {
    Canvas(
        Modifier
            .padding(tinyPadding / 2)
            .requiredSize(digitWidth, headerHeight)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val segments = mutableListOf<Path>()

        segments.add(Path().apply {  // top segment
            lineTo(DIGIT_STROKE, DIGIT_STROKE)
            lineTo(canvasWidth - DIGIT_STROKE, DIGIT_STROKE)
            lineTo(canvasWidth, 0F)
        })
        segments.add(Path().apply {  // top-right segment
            moveTo(canvasWidth, 0F)
            lineTo(canvasWidth - DIGIT_STROKE, DIGIT_STROKE)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight / 2 - DIGIT_STROKE / 2)
            lineTo(canvasWidth, canvasHeight / 2)
        })
        segments.add(Path().apply {  // midline segment
            moveTo(0F, canvasHeight / 2)
            lineTo(DIGIT_STROKE, canvasHeight / 2 - DIGIT_STROKE / 2)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight / 2 - DIGIT_STROKE / 2)
            lineTo(canvasWidth, canvasHeight / 2)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight / 2 + DIGIT_STROKE / 2)
            lineTo(DIGIT_STROKE, canvasHeight / 2 + DIGIT_STROKE / 2)
        })
        segments.add(Path().apply {  // top-left segment
            lineTo(DIGIT_STROKE, DIGIT_STROKE)
            lineTo(DIGIT_STROKE, canvasHeight / 2 - DIGIT_STROKE / 2)
            lineTo(0F, canvasHeight / 2)
        })
        segments.add(Path().apply {  // bottom-right segment
            moveTo(canvasWidth, canvasHeight / 2)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight / 2 + DIGIT_STROKE / 2)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight - DIGIT_STROKE)
            lineTo(canvasWidth, canvasHeight)
        })
        segments.add(Path().apply {  // bottom segment
            moveTo(0F, canvasHeight)
            lineTo(DIGIT_STROKE, canvasHeight - DIGIT_STROKE)
            lineTo(canvasWidth - DIGIT_STROKE, canvasHeight - DIGIT_STROKE)
            lineTo(canvasWidth, canvasHeight)
        })
        segments.add(Path().apply {  // bottom-left segment
            moveTo(0F, canvasHeight / 2)
            lineTo(DIGIT_STROKE, canvasHeight / 2 + DIGIT_STROKE / 2)
            lineTo(DIGIT_STROKE, canvasHeight - DIGIT_STROKE)
            lineTo(0F, canvasHeight)
        })

        for ((i, path) in segments.withIndex()) {
            drawPath(path = path, color = MinesweeperColors.onError, alpha = if (segment.isActive(i)) 1F else 0.15F)
        }
    }
}

@Preview
@Composable
private fun SegmentDigitPreview() {
    MinesweeperTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (segment in DigitSegment.values()) {
                SegmentDigit(segment)
            }
        }
    }
}

@Preview
@Composable
private fun DigitalScreenPreview() {
    MinesweeperTheme {
        Column {
            DigitalScreen(0)
            DigitalScreen(999)
            DigitalScreen(1)
            DigitalScreen(40)
            DigitalScreen(473)
        }
    }
}