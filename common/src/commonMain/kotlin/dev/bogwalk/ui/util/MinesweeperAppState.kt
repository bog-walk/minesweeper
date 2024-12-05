package dev.bogwalk.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.bogwalk.model.*

class MinesweeperAppState(
    val grid: Grid = GameGrid(Level.INTERMEDIATE.values[0], Level.INTERMEDIATE.values[1], Level.INTERMEDIATE.values[2]),
    val timer: Timer = Timer()
) {
    val flagsRemaining: Int
        get() = grid.flagsRemaining
    private val isGameWon: Boolean
        get() = grid.allMinesFound

    var gameState by mutableStateOf(GameState.PLAYING)
    var gameCells by mutableStateOf(grid.cells)

    fun leftClick(coordinates: Pair<Int, Int>) {
        if (timer.seconds == 0) timer.start()
        if (grid.selectCell(coordinates)) {
            if (isGameWon) {
                timer.end()
                gameState = GameState.WON
            }
        } else {
            // stepped on a mine
            timer.end()
            gameState = GameState.LOST
        }
        gameCells = grid.cells
    }

    fun rightClick(coordinates: Pair<Int, Int>) {
        if (timer.seconds == 0) timer.start()
        grid.flagCell(coordinates)
        gameCells = grid.cells
    }

    fun resetBoard() {
        timer.reset()
        grid.reset()
        gameState = GameState.PLAYING
        gameCells = grid.cells
    }
}