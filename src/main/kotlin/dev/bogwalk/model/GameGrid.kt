package dev.bogwalk.model

class GameGrid(
    private val numOfRows: Int = 16,
    private val numOfCols: Int = 16,
    private val numOfMines: Int = 40
) : Grid(numOfRows, numOfCols, numOfMines) {
    init {
        generateMineField()
    }

    override fun generateMineField() {
        val mines = (0 until numOfRows * numOfCols).shuffled().take(numOfMines)
        for (position in mines) {
            val row = position / numOfCols
            val col = position % numOfCols
            board[row][col] = board[row][col].copy(isMine = true, neighbourMines = -1)
            generateNeighbourCount(board[row][col])
        }
    }
}