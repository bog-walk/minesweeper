class MineField(
    private val fieldSize: Int,
    private val numMines: Int
) {
    private val minePositions = mutableListOf<Pair<Int, Int>>()
    val mines = minePositions.toList()

    init {
        generateMineField()
    }

    private fun generateMineField() {
        val mines: List<Int> = (0 until fieldSize * fieldSize).shuffled().take(numMines)
        for (i in 0 until numMines) {
            val row = mines[i] / fieldSize
            val col = mines[i] % fieldSize
            minePositions.add(Pair(row, col))
        }
    }
}