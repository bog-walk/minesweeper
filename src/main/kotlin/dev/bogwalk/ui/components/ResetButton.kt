package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import dev.bogwalk.ui.util.drawBevelEdge
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.GameState

@Composable
internal fun ResetButton(
    gameState: GameState,
    onResetRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(smallPadding)
            .requiredSize(headerHeight)
            .clickable(
                onClickLabel = FACE_CLICK,
                role = Role.Button,
                onClick = { onResetRequest() }
            )
            .background(color = MinesweeperColors.primary)
            .drawWithCache {
                onDrawBehind {
                    drawBevelEdge(BEVEL_STROKE_SM)
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
            modifier = Modifier.padding(tinyPadding),
            // must apply this to avoid icon being tinted black due to LocalContentColor
            tint = Color.Unspecified
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