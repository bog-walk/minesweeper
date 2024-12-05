package dev.bogwalk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Grid(
    val numOfRows: Int,
    val numOfCols: Int,
    protected val numOfMines: Int
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

    /**
     * Called by init() in each subclass to generate the count of surrounding mines prior to the start of gameplay.
     *
     * Rather than checking a Cell for surrounding mines when it is clicked, each mine that is created during Grid
     * initialisation invokes this function to increment the count of all its neighbours. A Cell defaults to a count
     * of 0, so any Cell that is far from a mine will still have the correct property-dependent behaviour when clicked.
     *
     * Note that coordinates have to be passed as args instead of Cell to avoid collection mutation errors during
     * potential recursive calls in expandSelection().
     */
    protected fun generateNeighbourCount(coordinates: Pair<Int, Int>) {
        for ((row, col) in getNeighbours(coordinates)) {
            val neighbour = board[row][col]
            board[row][col] = neighbour.copy(neighbourMines = neighbour.neighbourMines + 1)
        }
    }

    /**
     * Returns the coordinates of all neighbouring Cells that are not mines and have not been already clicked/flagged.
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
     * Toggles a Cell between Unselected and Flagged states.
     *
     * @throws IllegalArgumentException if attempting to flag a previously selected Cell (only possible in testing).
     */
    open fun flagCell(coordinates: Pair<Int, Int>) {
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
            // a selected Cell will not be able to invoke this due to the UI auto-disabling selected Cells
            CellState.SELECTED -> throw IllegalArgumentException("Cannot flag previously selected Cell")
        }
    }

    /**
     * Switches a valid Cell from Unselected to Selected state and returns false if the clicked Cell is a mine; otherwise,
     * returns true.
     *
     * If the amount of unselected Cells manages to decrement to the amount of generated mines without this function ever
     * having returned false (and thereby ending gameplay), this means all mines must have been successfully avoided and
     * the game has been won.
     *
     * @throws IllegalArgumentException if attempting to select a previously selected Cell (only possible in testing).
     */
    open fun selectCell(coordinates: Pair<Int, Int>): Boolean {
        val cell = board[coordinates.first][coordinates.second]
        return when (cell.state) {
            CellState.UNSELECTED -> {
                board[coordinates.first][coordinates.second] = cell.copy(state = CellState.SELECTED)
                unselectedCount--
                if (cell.isMine) {
                    false
                } else {
                    if (cell.neighbourMines == 0) {
                        expandSelection(coordinates)
                    }
                    if (unselectedCount == numOfMines) {
                        allMinesFound = true
                        // all unflagged mines should be flagged & count should drop accordingly.
                        // could toggle all remaining cells to Flagged state but current setup means the entire Grid
                        // would need to be checked (worse-case), so UI handles this based on change in overall game state
                        flagsRemaining = 0
                    }
                    true
                }
            }
            // a selected Cell will not be able to invoke this due to the UI auto-disabling selected Cells
            CellState.SELECTED -> throw IllegalArgumentException("Cell already previously selected")
            // calling this function on a flagged cell will have no effect
            // as a flagged Cell needs to stay enabled for potential right-click events
            CellState.FLAGGED -> true
        }
    }

    /**
     * Recursively auto-selects all surrounding Cells until a Cell that matches a base case is encountered:
     *  - it is a mine.
     *  - it has already been selected or flagged.
     *  - one of its neighbours is a mine (i.e. it has a surrounding count).
     */
    private fun expandSelection(coordinates: Pair<Int, Int>) {
        getNeighbours(coordinates).forEach { (r, c) ->
            val neighbour = board[r][c]
            if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                board[r][c] = neighbour.copy(state = CellState.SELECTED)
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
        for (row in 0 until numOfRows) {
            for (col in 0 until numOfCols) {
                board[row][col] = Cell(row to col)
            }
        }
        generateMineField()
    }
}