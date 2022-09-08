package dev.bogwalk.model

abstract class Grid(
    private val numOfRows: Int,
    private val numOfCols: Int,
    private val numOfMines: Int
) {
    var flagsRemaining = numOfMines
    var allMinesFound = false
    val board = Array(numOfRows) { row ->
        Array(numOfCols) { col ->
            Cell(row to col)
        }
    }
    private var unselectedCount = numOfRows * numOfCols

    abstract fun generateMineField()

    protected fun generateNeighbourCount(mine: Cell) {
        for (neighbour in getNeighbours(mine)) {
            neighbour.neighbourMines++
        }
    }

    /**
     * Returns all neighbouring Cells that are not mines or are not flagged(???).
     */
    private fun getNeighbours(cell: Cell): List<Cell> {
        val (row, col) = cell.coordinates
        val n = listOf(-1, 0, 1)
        val rows = n.map { (it + row).coerceIn(0, numOfRows - 1) }.toSet()
        val cols = n.map { (it + col).coerceIn(0, numOfCols - 1) }.toSet()
        val neighbours = mutableListOf<Cell>()
        for (x in rows) {
            for (y in cols) {
                if (x == row && y == col) continue
                val neighbour = board[x][y]
                if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                    neighbours.add(neighbour)
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
                flagsRemaining--
                cell.state = CellState.FLAGGED
            }
            CellState.FLAGGED -> {
                flagsRemaining++
                cell.state = CellState.UNSELECTED
            }
            // calling this function on a selected cell will not be possible through UI disabling the cell
            else -> throw IllegalArgumentException("Cannot flag previously selected Cell")
        }
    }

    /**
     * Changes state of selected Cell is previously unselected & returns true if the selected Cell is a mine.
     *
     * If the amount of unselected Cells manages to decrement to the amount of hidden mines without this function ever
     * having returned true, this means all mines must have been avoided correctly and the game has been won.
     */
    fun selectCell(coordinates: Pair<Int, Int>): Boolean {
        require(coordinates.first in 0 until numOfRows &&
                coordinates.second in 0 until numOfCols) { "Invalid coordinates" }
        val cell = board[coordinates.first][coordinates.second]
        return when (cell.state) {
            CellState.UNSELECTED -> {
                unselectedCount--
                cell.state = CellState.SELECTED
                if (cell.isMine) {
                    false
                } else if (unselectedCount == numOfMines) {
                    allMinesFound = true
                    true
                } else {
                    if (cell.neighbourMines == 0) {
                        //println("About to enter...")
                        expandSelection(cell)
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

    private fun expandSelection(cell: Cell) {
        //println("In expandSelection() for ${cell.coordinates}")
        getNeighbours(cell).forEach { neighbour ->
            if (neighbour.state == CellState.UNSELECTED && !neighbour.isMine) {
                //println("For neighbour ${neighbour.coordinates}")
                unselectedCount--
                neighbour.state = CellState.SELECTED
                if (neighbour.neighbourMines == 0) {
                    expandSelection(neighbour)
                }
            }
        }
    }
}