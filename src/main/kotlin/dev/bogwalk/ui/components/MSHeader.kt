package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.drawBevelEdge
import dev.bogwalk.ui.style.*

@Composable
fun MSHeader(
    flagsRemaining: Int,
    seconds: Int,
    gameState: GameState,
    onResetRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = MinesweeperColors.primary)
            .padding(windowPadding)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawBevelEdge(BEVEL_STROKE * 2, isElevated = false)
                }
            }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DigitalScreen(flagsRemaining)
        ResetButton(gameState, onResetRequest)
        DigitalScreen(seconds)
    }
}

@Preview
@Composable
private fun MSHeaderPreview() {
    MinesweeperTheme {
        Box(Modifier.width(cellSize * 12)) {
            MSHeader(10, 23, GameState.PLAYING) {}
        }
    }
}