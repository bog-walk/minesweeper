package dev.bogwalk.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.ui.components.MSGrid
import dev.bogwalk.ui.components.MSHeader
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.util.MinesweeperAppState

@Composable
fun MinesweeperApp(state: MinesweeperAppState = MinesweeperAppState()) {
    val minesweeperState = remember { state }

    Column(
        // this modifier forces header to match dynamic width of grid
        modifier = Modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MSHeader(
            minesweeperState.flagsRemaining,
            minesweeperState.timer.seconds,
            minesweeperState.gameState,
            minesweeperState::resetBoard
        )
        MSGrid(
            minesweeperState.grid.numOfRows,
            minesweeperState.grid.numOfCols,
            minesweeperState.gameCells,
            minesweeperState.gameState,
            minesweeperState::leftClick
        ) {
            minesweeperState.rightClick(it)
        }
    }
}

@Preview
@Composable
private fun MinesweeperAppPreview() {
    MinesweeperTheme {
        MinesweeperApp()
    }
}