package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import dev.bogwalk.ui.util.drawBevelEdge
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.GameState

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
                onDrawBehind {
                    drawBevelEdge(BEVEL_STROKE_LR, isElevated = false)
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