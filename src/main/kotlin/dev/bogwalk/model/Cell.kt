package dev.bogwalk.model

data class Cell(
    val coordinates: Pair<Int, Int>,
    val isMine: Boolean = false,
    var neighbourMines: Int = 0,
    var state: CellState = CellState.UNSELECTED
) {
    override fun toString(): String {
        return when (state) {
            CellState.UNSELECTED -> " "
            CellState.FLAGGED -> "?"
            CellState.SELECTED -> if (isMine) "X" else neighbourMines.toString()
        }
    }
}