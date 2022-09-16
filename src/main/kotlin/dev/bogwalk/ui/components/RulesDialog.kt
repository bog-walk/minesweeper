package dev.bogwalk.ui.components

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.style.*

@Composable
fun RulesDialog(
    onCloseRequest: () -> Unit
) {
    Dialog(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogWidth, dialogHeight)),
        title = RULES_MENU,
        resizable = false
    ) {
        MSRules()
    }
}

@Composable
private fun MSRules() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DigitalScreen(10)
            ResetButton(GameState.PLAYING) {}
            DigitalScreen(999)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LEFT_SCREEN,
                modifier = Modifier.width(headerHeight * 3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = FACE_CLICK,
                modifier = Modifier.width(headerHeight * 3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = RIGHT_SCREEN,
                modifier = Modifier.width(headerHeight * 3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(Modifier.height(windowPadding))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MouseIcon()
            MouseIcon(isLeftClick = false)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LEFT_CLICK,
                modifier = Modifier.width(headerHeight * 3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = RIGHT_CLICK,
                modifier = Modifier.width(headerHeight * 3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MouseIcon(isLeftClick: Boolean = true) {
    Canvas(
        modifier = Modifier
            .padding(smallPadding)
            .requiredSize(width = mouseWidth, height = mouseHeight),
        contentDescription = if (isLeftClick) LEFT_CLICK_DESCRIPTION else RIGHT_CLICK_DESCRIPTION
    ) {
        val xOffsets = if (isLeftClick) listOf(4f, 7f, 2f, 5f, 6f, 10f) else listOf(-4f, -7f, -2f, -5f, -6f, -10f)
        drawLine(MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[0]) % size.width, 4f),
            Offset((size.width + xOffsets[1]) % size.width, 6f),
            DIGIT_STROKE / 2,
            StrokeCap.Round
        )
        drawLine(MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[2]) % size.width, 8f),
            Offset((size.width + xOffsets[3]) % size.width, 9f),
            DIGIT_STROKE / 2,
            StrokeCap.Round
        )
        drawLine(MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[4]) % size.width, 0f),
            Offset((size.width + xOffsets[5]) % size.width, 4f),
            DIGIT_STROKE / 2,
            StrokeCap.Round
        )

        val bottom = Path().apply {
            moveTo(MOUSE_X_OFFSET, size.height / 2 - 10)
            lineTo(size.width - MOUSE_X_OFFSET, size.height / 2 - 10)
            arcTo(
                Rect(topLeft = Offset(MOUSE_X_OFFSET, size.height / 2 - 10),
                    bottomRight = Offset(size.width - MOUSE_X_OFFSET, size.height - MOUSE_Y_OFFSET)),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(bottom, color = MinesweeperColors.onPrimary, style = Stroke(width = DIGIT_STROKE))

        val left = Path().apply {
            moveTo(size.width / 2 - 3, MOUSE_Y_OFFSET)
            lineTo(size.width / 2 - 3, size.height / 2 - 15)
            lineTo(MOUSE_X_OFFSET, size.height / 2 - 15)
            arcTo(
                Rect(topLeft = Offset(MOUSE_X_OFFSET, MOUSE_Y_OFFSET),
                    bottomRight = Offset(size.width / 2 - 3, size.height / 2 - 15)),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(left, MinesweeperColors.onPrimary, style = if (isLeftClick) Fill else Stroke(width = DIGIT_STROKE))

        val right = Path().apply {
            moveTo(size.width - MOUSE_X_OFFSET, size.height / 2 - 15)
            lineTo(size.width / 2 + 3, size.height / 2 - 15)
            lineTo(size.width / 2 + 3, MOUSE_Y_OFFSET)
            arcTo(
                Rect(topLeft = Offset(size.width / 2 + 3, MOUSE_Y_OFFSET),
                    bottomRight = Offset(size.width - MOUSE_X_OFFSET, size.height / 2 - 15)),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(right, MinesweeperColors.onPrimary, style = if (isLeftClick) Stroke(width = DIGIT_STROKE) else Fill)
    }
}

@Preview
@Composable
private fun RulesDialogPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogWidth, height = dialogHeight)
            .border(1.dp, Color.Red)) {
            MSRules()
        }
    }
}

@Preview
@Composable
private fun AnimatedClickIconPreview() {
    MinesweeperTheme {
        Row {
            MouseIcon(isLeftClick = true)
            MouseIcon(isLeftClick = false)
        }
    }
}