class GameBoard(
    private val fieldSize: Int,
    private val numMines: Int
) {
    private val board = Array(fieldSize) { row ->
        Array(fieldSize) { col ->
            Cell(row, col)
        }
    }
    private val predictions = mutableSetOf<Cell>()

    init {
        generateMineField()
    }

    val size: Int
        get() = fieldSize

    private fun generateMineField() {
        val mines: List<Int> = (0 until fieldSize * fieldSize).shuffled().take(numMines)
        for (i in 0 until numMines) {
            val row = mines[i] / fieldSize
            val col = mines[i] % fieldSize
            board[row][col].isMine = true
        }
    }

    fun getCell(x: Int, y: Int): Cell {
        try {
            return board[x][y]
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("Cell coordinates must be between 1 and $fieldSize inclusive.")
        }
    }

    private fun getNeighbours(cell: Cell): List<Cell> {
        val n = listOf(-1, 0, 1)
        val row = n.map { (it + cell.xCoord).coerceIn(0, fieldSize - 1) }.toSet()
        val col = n.map { (it + cell.yCoord).coerceIn(0, fieldSize - 1) }.toSet()
        val neighbours = mutableListOf<Cell>()
        for (x in row) {
            for (y in col) {
                if (x == cell.xCoord && y == cell.yCoord) continue
                val neighbour = board[x][y]
                if (neighbour.state == CellState.EMPTY) {
                    neighbours.add(neighbour)
                }
            }
        }
        return neighbours
    }

    // Should marked neighbours be explored?
    fun exploreNeighbours(cell: Cell) {
        if (cell.state == CellState.EMPTY || cell.state == CellState.MARKED) {
            val neighbours = getNeighbours(cell)
            cell.neighbourMines = neighbours.count { it.isMine }
            if (!cell.isMine) {
                cell.state = CellState.SAFE
            }
            neighbours.forEach { neighbour ->
                if (!neighbour.isMine && neighbour.state != CellState.SAFE) {
                    exploreNeighbours(neighbour)
                }
            }
        }
    }

    fun checkMinesMarked(): Boolean {
        return predictions.size == numMines && predictions.all { it.foundMine }
    }

    // Checks if cells remaining to either be predicted or have already been incorrectly marked
    // Is this necessary or is checkMinesMarked() enough?
    fun checkRemainingCells(): Boolean {
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                val cell = board[i][j]
                if (cell.state == CellState.EMPTY ||
                    cell.state == CellState.MARKED && !cell.isMine) return false
            }
        }
        return true
    }

    fun showAllMines() {
        board.forEach { row ->
            row.forEach { cell ->
                if (cell.isMine) {
                    cell.state = CellState.MINE
                }
            }
        }
    }

    // Only works for predicting as a mine or unmarking a mine
    fun predict(cell: Cell, asMine: Boolean = true) {
        if (asMine) {
            cell.state = CellState.MARKED
            this.predictions.add(cell)
        } else {
            cell.state = CellState.EMPTY
            this.predictions.remove(cell)
        }
    }

    fun drawGameBoard(): String {
        var output = " |${(1..fieldSize).joinToString("")}|\n" +
                "-|${"-".repeat(fieldSize)}|\n"
        for (i in 0 until fieldSize) {
            output += "${i + 1}|"
            for (j in 0 until fieldSize) {
                output += board[i][j]
            }
            output += "|\n"
        }
        return output + "-|${"-".repeat(fieldSize)}|"
    }
}