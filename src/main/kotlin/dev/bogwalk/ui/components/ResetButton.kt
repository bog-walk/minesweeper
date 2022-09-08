package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.drawBevelEdge
import dev.bogwalk.ui.style.*

@Composable
fun ResetButton(
    gameState: GameState,
    onResetRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(smallPadding)
            .requiredSize(headerHeight)
            .clickable(
                onClickLabel = null,
                role = Role.Button,
                onClick = { onResetRequest() }
            )
            .background(color = MinesweeperColors.primary)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawBevelEdge(BEVEL_STROKE)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(when (gameState) {
                GameState.PLAYING -> RESET_DEFAULT_ICON
                GameState.WON -> RESET_WON_ICON
                GameState.LOST -> RESET_LOST_ICON
            }),
            contentDescription = when (gameState) {
                GameState.PLAYING -> RESET_DEFAULT_DESCRIPTION
                GameState.WON -> RESET_WON_DESCRIPTION
                GameState.LOST -> RESET_LOST_DESCRIPTION
            },
            modifier = Modifier.matchParentSize().padding(tinyPadding),
            tint = Color.White
        )
    }
}

@Preview
@Composable
private fun ResetButtonPreview() {
    MinesweeperTheme {
        Column {
            ResetButton(GameState.PLAYING) {}
            ResetButton(GameState.WON) {}
            ResetButton(GameState.LOST) {}
        }
    }
}