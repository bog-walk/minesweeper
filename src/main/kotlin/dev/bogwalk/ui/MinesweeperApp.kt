package dev.bogwalk.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.ui.components.MSGrid
import dev.bogwalk.ui.components.MSHeader
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.util.MinesweeperAppState

@Composable
fun MinesweeperApp(state: MinesweeperAppState) {
    Column(
        // this modifier forces MSHeader to match dynamic width of MSGrid
        modifier = Modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MSHeader(
            state.flagsRemaining,
            state.timer.seconds,
            state.gameState,
            state::resetBoard
        )
        MSGrid(
            state.grid.numOfRows,
            state.grid.numOfCols,
            state.gameCells,
            state.gameState,
            state::leftClick
        ) {
            state.rightClick(it)
        }
    }
}

@Preview
@Composable
private fun MinesweeperAppPreview() {
    MinesweeperTheme {
        MinesweeperApp(MinesweeperAppState())
    }
}