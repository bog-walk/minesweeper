package dev.bogwalk.model

class TestGrid(
    private val mines: List<Pair<Int, Int>>,
    numOfRows: Int = 5,
    numOfCols: Int = 5
) : Grid(numOfRows, numOfCols, mines.size) {
    init {
        generateMineField()
    }

    override fun generateMineField() {
        for ((row, col) in mines) {
            board[row][col] = board[row][col].copy(neighbourMines = -1)
            generateNeighbourCount(row to col)
        }
    }

    fun drawBoard(countsOnly: Boolean = false): String {
        val output = StringBuilder().append((1..numOfCols).joinToString("", " |", "|\n"))
        output.append("-|${"-".repeat(numOfCols)}|\n")
        for (i in 0 until numOfRows) {
            output.append("${i + 1}|")
            for (j in 0 until numOfCols) {
                val cell = board[i][j]
                if (countsOnly) {
                    output.append(if (cell.isMine) "X" else cell.neighbourMines.toString())
                } else {
                    output.append(cell)
                }
            }
            output.append("|\n")
        }
        output.append("-|${"-".repeat(numOfCols)}|")
        return output.toString()
    }
}