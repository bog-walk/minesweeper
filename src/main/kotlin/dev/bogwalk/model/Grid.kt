package dev.bogwalk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Grid(
    val numOfRows: Int,
    val numOfCols: Int,
    val numOfMines: Int
) {
    protected val board = List(numOfRows) { row ->
        MutableList(numOfCols) { col ->
            Cell(row to col)
        }
    }
    val cells: List<Cell>
        get() = board.flatten()

    var flagsRemaining by mutableStateOf(numOfMines)
    var allMinesFound by mutableStateOf(false)

    private var unselectedCount = numOfRows * numOfCols

    abstract fun generateMineField()

    // coordinates have to be passed as args instead of Cell to avoid mutation error in recursive call
    protected fun generateNeighbourCount(coordinates: Pair<Int, Int>) {
        for ((row, col) in getNeighbours(coordinates)) {
            val neighbour = board[row][col]
            board[row][col] = board[row][col].copy(neighbourMines = neighbour.neighbourMines + 1)
        }
    }

    /**
     * Returns all neighbouring Cells that are not mines or are not flagged(???).
     */
    private fun getNeighbours(coordinates: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (row, col) = coordinates
        val n = listOf(-1, 0, 1)
        val rows = n.map { (it + row).coerceIn(0, numOfRows - 1) }.toSet()
        val cols = n.map { (it + col).coerceIn(0, numOfCols - 1) }.toSet()
        val neighbours = mutableListOf<Pair<Int, Int>>()
        for (x in rows) {
            for (y in cols) {
                if (x == row && y == col) continue
                val neighbour = board[x][y]
                if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                    neighbours.add(neighbour.coordinates)
                }
            }
        }
        return neighbours
    }

    /**
     * Toggles a Cell as CellState.Flagged.
     */
    fun flagCell(coordinates: Pair<Int, Int>) {
        require(coordinates.first in 0 until numOfRows &&
                coordinates.second in 0 until numOfCols) { "Invalid coordinates" }
        val cell = board[coordinates.first][coordinates.second]
        when (cell.state) {
            CellState.UNSELECTED -> {
                if (flagsRemaining == 0) return
                flagsRemaining--
                board[coordinates.first][coordinates.second] = cell.copy(state = CellState.FLAGGED)
            }
            CellState.FLAGGED -> {
                flagsRemaining++
                board[coordinates.first][coordinates.second] = cell.copy(state = CellState.UNSELECTED)
            }
            // calling this function on a selected cell will not be possible through UI disabling the cell
            else -> throw IllegalArgumentException("Cannot flag previously selected Cell")
        }
    }

    /**
     * Changes state of selected Cell fs previously unselected & returns false only if the selected Cell is a mine.
     *
     * If the amount of unselected Cells manages to decrement to the amount of hidden mines without this function ever
     * having returned false, this means all mines must have been avoided correctly and the game has been won.
     */
    fun selectCell(coordinates: Pair<Int, Int>): Boolean {
        require(coordinates.first in 0 until numOfRows &&
                coordinates.second in 0 until numOfCols) { "Invalid coordinates" }
        val cell = board[coordinates.first][coordinates.second]
        return when (cell.state) {
            CellState.UNSELECTED -> {
                board[coordinates.first][coordinates.second] = cell.copy(state = CellState.SELECTED)
                unselectedCount--
                if (cell.isMine) {
                    //selectedAMine = true
                    false
                } else if (unselectedCount == numOfMines) {
                    allMinesFound = true
                    true
                } else {
                    if (cell.neighbourMines == 0) {
                        expandSelection(coordinates)
                    }
                    true
                }
            }
            // calling this function on a selected cell will not be possible through UI disabling the cell
            CellState.SELECTED -> throw IllegalArgumentException("Cell already previously selected")
            // calling this function on a flagged cell will have no effect
            CellState.FLAGGED -> true
        }
    }

    private fun expandSelection(coordinates: Pair<Int, Int>) {
        getNeighbours(coordinates).forEach { (r, c) ->
            val neighbour = board[r][c]
            if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                board[neighbour.coordinates.first][neighbour.coordinates.second] = neighbour.copy(state = CellState.SELECTED)
                unselectedCount--
                if (neighbour.neighbourMines == 0) {
                    expandSelection(neighbour.coordinates)
                }
            }
        }
    }

    fun reset() {
        flagsRemaining = numOfMines
        allMinesFound = false
        unselectedCount = numOfRows * numOfCols
        for ((i, row) in board.withIndex()) {
            for (j in row.indices) {
                board[i][j] = Cell(i to j)
            }
        }
        generateMineField()
    }
}