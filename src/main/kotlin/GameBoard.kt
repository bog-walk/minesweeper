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

    fun exploreCellsRevised(cell: Cell) {
        while (cell.state == CellState.EMPTY || cell.state == CellState.MARKED) {
            val n = listOf(-1, 0, 1)
            val x = n.map { (it + cell.xCoord).coerceIn(0, fieldSize) }.toSet()
            val y = n.map { (it + cell.yCoord).coerceIn(0, fieldSize) }.toSet()
            val neighbours = mutableListOf<Cell>()
            for (i in y) {
                for (j in x) {
                    if (i == cell.yCoord && j == cell.xCoord) continue
                    neighbours.add(board[i][j])
                }
            }
            val count = neighbours.count { it.isMine }
            if (count > 0) {
                cell.neighbourMines = count
            } else {
                cell.state = CellState.SAFE
            }
            neighbours.forEach { neighbour ->
                if (!neighbour.isMine && neighbour.state != CellState.SAFE) {
                    exploreCellsRevised(cell)
                }
            }
        }
    }

    fun checkMinesMarked(): Boolean {
        return predictions.size == numMines && predictions.all { it.foundMine }
    }

    fun checkRemainingCells(): Boolean {
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                val cell = board[i][j]
                if ((cell.state == CellState.EMPTY || cell.state == CellState.MARKED) &&
                    !cell.isMine) return false
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

    fun predict(cell: Cell, isMine: Boolean = true) {
        if (isMine) {
            this.predictions.add(cell)
        } else {
            this.predictions.remove(cell)
        }
    }

    fun drawGameBoard() {
        println(" |${(1..fieldSize).joinToString("")}|")
        println("-|${"-".repeat(fieldSize)}|")
        for (i in 0 until fieldSize) {
            print("${i + 1}|")
            for (j in 0 until fieldSize) {
                print(board[i][j])
            }
            print("|\n")
        }
        println("-|${"-".repeat(fieldSize)}|")
    }
}