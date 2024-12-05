package dev.bogwalk.ui.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.ui.components.DigitalScreen
import dev.bogwalk.ui.components.MSCell
import dev.bogwalk.ui.components.ResetButton
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.GameState
import org.jetbrains.compose.resources.stringResource

@Composable
fun RulesDialog(
    onCloseRequest: () -> Unit
) {
    DialogWindow(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogSize, dialogSize)),
        title = stringResource(Res.string.rules_menu),
        resizable = false,
        content = { MSRules() }
    )
}

@Composable
private fun MSRules() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val rulesWidth = headerHeight * 3

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
                text = stringResource(Res.string.left_screen),
                modifier = Modifier.width(rulesWidth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(Res.string.face_click),
                modifier = Modifier.width(rulesWidth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(Res.string.right_screen),
                modifier = Modifier.width(rulesWidth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(Modifier.height(windowPadding))
        Row(
            modifier = Modifier.padding(vertical = tinyPadding).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MSCell(GameState.WON, Cell(0 to 0, neighbourMines = 3, state = CellState.SELECTED), {}, {})
        }
        Row(
            modifier = Modifier.padding(vertical = tinyPadding).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.number_cell),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(Modifier.height(windowPadding))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MouseIcon(isLeftClick = true)
            MouseIcon(isLeftClick = false)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.left_click),
                modifier = Modifier.width(rulesWidth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(Res.string.right_click),
                modifier = Modifier.width(rulesWidth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun MouseIcon(isLeftClick: Boolean) {
    val mouseWidth = 70.dp
    val mouseHeight = 80.dp

    Canvas(
        modifier = Modifier
            .padding(smallPadding)
            .requiredSize(mouseWidth, mouseHeight),
        contentDescription = stringResource(
            if (isLeftClick) Res.string.left_click_description else Res.string.right_click_description
        )
    ) {
        val strokeWidth = 2f

        val xOffsets = if (isLeftClick) listOf(4f, 7f, 2f, 5f, 6f, 10f) else listOf(-4f, -7f, -2f, -5f, -6f, -10f)
        // small strokes depicting movement
        drawLine(
            MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[0]) % size.width, 4f),
            Offset((size.width + xOffsets[1]) % size.width, 6f),
            strokeWidth,
            StrokeCap.Round
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[2]) % size.width, 8f),
            Offset((size.width + xOffsets[3]) % size.width, 9f),
            strokeWidth,
            StrokeCap.Round
        )
        drawLine(
            MinesweeperColors.onPrimary,
            Offset((size.width + xOffsets[4]) % size.width, 0f),
            Offset((size.width + xOffsets[5]) % size.width, 4f),
            strokeWidth,
            StrokeCap.Round
        )

        val xOffset = 8f
        val yOffset = 5f

        val bottom = Path().apply {
            moveTo(xOffset, size.height / 2 - 10)
            lineTo(size.width - xOffset, size.height / 2 - 10)
            arcTo(
                Rect(
                    topLeft = Offset(xOffset, size.height / 2 - 10),
                    bottomRight = Offset(size.width - xOffset, size.height - yOffset)
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(bottom, color = MinesweeperColors.onPrimary, style = Stroke(width = strokeWidth))

        val buttonXOffset = 3f
        val buttonYOffset = 15f

        val left = Path().apply {
            moveTo(size.width / 2 - buttonXOffset, yOffset)
            lineTo(size.width / 2 - buttonXOffset, size.height / 2 - buttonYOffset)
            lineTo(xOffset, size.height / 2 - buttonYOffset)
            arcTo(
                Rect(
                    topLeft = Offset(xOffset, yOffset),
                    bottomRight = Offset(size.width / 2 - buttonXOffset, size.height / 2 - buttonYOffset)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(left, MinesweeperColors.onPrimary, style = if (isLeftClick) Fill else Stroke(width = strokeWidth))

        val right = Path().apply {
            moveTo(size.width - xOffset, size.height / 2 - buttonYOffset)
            lineTo(size.width / 2 + buttonXOffset, size.height / 2 - buttonYOffset)
            lineTo(size.width / 2 + buttonXOffset, yOffset)
            arcTo(
                Rect(
                    topLeft = Offset(size.width / 2 + buttonXOffset, yOffset),
                    bottomRight = Offset(size.width - xOffset, size.height / 2 - buttonYOffset)
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(right, MinesweeperColors.onPrimary, style = if (isLeftClick) Stroke(width = strokeWidth) else Fill)
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

@Preview
@Composable
private fun RulesDialogPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogSize, height = dialogSize)
            .border(tinyPadding, Color.Red)) {
            MSRules()
        }
    }
}