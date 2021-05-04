class PlayingField(
    private val fieldSize: Int,
    private val numMines: Int
) {
    private val board = Array(fieldSize) { Array(fieldSize) { CellState.EMPTY.mark } }
    private val mineField = MineField(fieldSize, numMines)
    private val predictedMines = mutableListOf<Pair<Int, Int>>()

    private fun exploreCellsRevised(row: Int, col: Int) {
        while (board[row][col] == CellState.EMPTY.mark || board[row][col] == CellState.MARKED.mark) {
            val n = listOf(-1, 0, 1)
            val x = n.map { it + col }
            val y = n.map { it + row }
            val neighbours = mutableListOf<Pair<Int, Int>>()
            for (i in y) {
                for (j in x) {
                    if (i == row && j == col) continue
                    neighbours.add(Pair(i, j))
                }
            }
            val count = neighbours.count { it in mineField.mines }
            board[row][col] = if (count > 0) count.toString() else "/"
            for (pair in neighbours) {
                val y = pair.first
                val x = pair.second
                if (y in 0 until fieldSize && x in 0 until fieldSize
                    && Pair(y, x) !in mineField.mines && board[y][x] != CellState.SAFE.mark) {
                    exploreCellsRevised(y, x)
                }
            }
        }
    }

    fun checkMinesMarked(): Boolean {
        return predictedMines.isNotEmpty() &&
                predictedMines.size == mineField.mines.size &&
                predictedMines.toTypedArray().contentDeepEquals(mineField.mines.toTypedArray())
    }

    fun checkRemainingCells(): Boolean {
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                if ((board[i][j] == CellState.EMPTY.mark || board[i][j] == CellState.MARKED.mark) &&
                    Pair(i, j) !in mineField.mines) return false
            }
        }
        return true
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