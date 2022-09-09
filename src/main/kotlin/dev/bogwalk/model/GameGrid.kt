package dev.bogwalk.model

class GameGrid(
    numOfRows: Int = 9,
    numOfCols: Int = 9,
    numOfMines: Int = 10
) : Grid(numOfRows, numOfCols, numOfMines) {
    init {
        generateMineField()
    }

    override fun generateMineField() {
        val mines = (0 until numOfRows * numOfCols).shuffled().take(numOfMines)
        for (position in mines) {
            val row = position / numOfCols
            val col = position % numOfCols
            board[row][col] = board[row][col].copy(neighbourMines = -1)
            generateNeighbourCount(row to col)
        }
    }
}