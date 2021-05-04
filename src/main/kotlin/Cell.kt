data class Cell(
    val xCoord: Int,
    val yCoord: Int
) {
    // This is the state on the gameboard visible to the player
    var state: CellState = CellState.EMPTY
    var neighbourMines = 0
    var isMine = false
    val foundMine: Boolean
        get() = isMine && (state == CellState.MARKED || state == CellState.MINE)

    override fun toString(): String {
        return if (neighbourMines > 0) "$neighbourMines" else state.mark
    }
}
