package dev.bogwalk.model

class GameGrid(
    numOfRows: Int = 16,
    numOfCols: Int = 16,
    numOfMines: Int = 40
) : Grid(numOfRows, numOfCols, numOfMines) {
    init {
        println("Generating $numOfRows by $numOfCols grid with $numOfMines mines")
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