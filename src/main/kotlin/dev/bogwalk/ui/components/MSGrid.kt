package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.drawBevelEdge
import dev.bogwalk.ui.style.BEVEL_STROKE
import dev.bogwalk.ui.style.MinesweeperColors
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.style.windowPadding

@Composable
fun MSGrid(
    board: Array<Array<Cell>>,
    gameState: GameState,
    onSelectCell: (Cell) -> Unit,
    onFlagCell: (Cell) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MinesweeperColors.primary)
            .padding(windowPadding)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawBevelEdge(BEVEL_STROKE * 2, isElevated = false)
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for ((i, row) in board.withIndex()) {
            key("Row $i") {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for ((j, cell) in row.withIndex()) {
                        key("Cell $i $j") {
                            MSCell(cell, gameState, onSelectCell, onFlagCell)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MSGridPreview_Initial() {
    val board = Array(9) { r -> Array(9) { c -> Cell(r to c) } }
    MinesweeperTheme {
        MSGrid(board, GameState.PLAYING, {}, {})
    }
}

@Preview
@Composable
private fun MSGridPreview_AllEmpty() {
    val board = Array(9) { r -> Array(9) { c -> Cell(r to c, state = CellState.SELECTED) } }
    MinesweeperTheme {
        MSGrid(board, GameState.PLAYING, {}, {})
    }
}

@Preview
@Composable
private fun MSGridPreview_GameWon() {
    val board = Array(9) { r -> Array(9) { c -> Cell(r to c, state = CellState.SELECTED) } }

    for ((r, c) in listOf(0 to 3, 3 to 0, 3 to 1, 3 to 2, 6 to 8)) {
        board[r][c] = board[r][c].copy(isMine = true, neighbourMines = -1, state = CellState.FLAGGED)
    }
    for ((r, c) in listOf(
        0 to 2, 0 to 4, 1 to 2, 1 to 3, 1 to 4, 2 to 3, 3 to 3, 4 to 3, 5 to 7, 5 to 8, 6 to 7, 7 to 7, 7 to 8)
    ) {
        board[r][c] = board[r][c].copy(neighbourMines = 1)
    }
    for ((r, c) in listOf(2 to 0, 2 to 2, 4 to 0, 4 to 2)) {
        board[r][c] = board[r][c].copy(neighbourMines = 2)
    }
    for ((r, c) in listOf(2 to 1, 4 to 1)) {
        board[r][c] = board[r][c].copy(neighbourMines = 3)
    }

    MinesweeperTheme {
        MSGrid(board, GameState.WON, {}, {})
    }
}
