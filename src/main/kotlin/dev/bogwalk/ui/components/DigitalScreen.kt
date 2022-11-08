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
import androidx.compose.ui.platform.testTag
import dev.bogwalk.ui.style.*

/**
 * Uses bit masking to store multiple flag values representing whether each segment in a retro 7-segment clock is
 * supposed to be on to display the digit.
 *
 *        0
 *      """""
 *    3 "   " 1
 *      "   "
 *        2
 *      """""
 *      "   "
 *    6 "   " 4
 *      """""
 *        5
 *
 * e.g. 7 should be displayed with the top, top right, and bottom right segments on -> 0b0010011.
 */
enum class DigitSegment(private val cache: Int) {
    ZERO(0b1111011),
    ONE(0b0010010),
    TWO(0b1100111),
    THREE(0b0110111),
    FOUR(0b0011110),
    FIVE(0b0111101),
    SIX(0b1111101),
    SEVEN(0b0010011),
    EIGHT(0b1111111),
    NINE(0b0111111);

    fun isActive(pathIndex: Int): Boolean = (cache shr pathIndex) and 1 == 1

    companion object {
        fun fromDigit(digit: Int): DigitSegment = values()[digit]
    }
}

@Composable
internal fun DigitalScreen(
    count: Int
) {
    Row(
        modifier = Modifier
            .testTag(DIGITAL_TEST_TAG)
            .background(MinesweeperColors.onPrimary),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var remainder = count
        var pow = 100

        repeat(3) {
            key("Digit $it") {
                SegmentDigit(DigitSegment.fromDigit(remainder / pow))

                remainder %= pow
                pow /= 10
            }
        }
    }
}

@Composable
private fun SegmentDigit(
    segment: DigitSegment
) {
    Canvas(
        Modifier
            .testTag("Digit ${segment.name}")
            .padding(tinyPadding / 2)
            .requiredSize(windowPadding, headerHeight)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val origin = 0f
        val digitStroke = 4f

        val segments = listOf(
            Path().apply {  // top segment
                lineTo(digitStroke, digitStroke)
                lineTo(canvasWidth - digitStroke, digitStroke)
                lineTo(canvasWidth, origin)
            },
            Path().apply {  // top-right segment
                moveTo(canvasWidth, origin)
                lineTo(canvasWidth - digitStroke, digitStroke)
                lineTo(canvasWidth - digitStroke, canvasHeight / 2 - digitStroke / 2)
                lineTo(canvasWidth, canvasHeight / 2)
            },
            Path().apply {  // midline segment
                moveTo(origin, canvasHeight / 2)
                lineTo(digitStroke, canvasHeight / 2 - digitStroke / 2)
                lineTo(canvasWidth - digitStroke, canvasHeight / 2 - digitStroke / 2)
                lineTo(canvasWidth, canvasHeight / 2)
                lineTo(canvasWidth - digitStroke, canvasHeight / 2 + digitStroke / 2)
                lineTo(digitStroke, canvasHeight / 2 + digitStroke / 2)
            },
            Path().apply {  // top-left segment
                lineTo(digitStroke, digitStroke)
                lineTo(digitStroke, canvasHeight / 2 - digitStroke / 2)
                lineTo(origin, canvasHeight / 2)
            },
            Path().apply {  // bottom-right segment
                moveTo(canvasWidth, canvasHeight / 2)
                lineTo(canvasWidth - digitStroke, canvasHeight / 2 + digitStroke / 2)
                lineTo(canvasWidth - digitStroke, canvasHeight - digitStroke)
                lineTo(canvasWidth, canvasHeight)
            },
            Path().apply {  // bottom segment
                moveTo(origin, canvasHeight)
                lineTo(digitStroke, canvasHeight - digitStroke)
                lineTo(canvasWidth - digitStroke, canvasHeight - digitStroke)
                lineTo(canvasWidth, canvasHeight)
            },
            Path().apply {  // bottom-left segment
                moveTo(origin, canvasHeight / 2)
                lineTo(digitStroke, canvasHeight / 2 + digitStroke / 2)
                lineTo(digitStroke, canvasHeight - digitStroke)
                lineTo(origin, canvasHeight)
            }
        )

        for ((i, path) in segments.withIndex()) {
            drawPath(path = path, color = MinesweeperColors.onError, alpha = if (segment.isActive(i)) 1f else 0.15f)
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