package dev.bogwalk.model

data class Cell(
    val xCoord: Int,
    val yCoord: Int
) {
    // This is the state on the gameboard visible to the player
    var state: CellState = CellState.EMPTY
    // This should only be propagated when player explores the cell's neighbours
    var neighbourMines = 0
    var isMine = false
    // Should this take into consideration when state == MINE or not since
    // latter only occurs when user steps on mine & game over
    val foundMine: Boolean
        get() = isMine && state == CellState.MARKED

    override fun toString(): String {
        return if (!isMine && neighbourMines > 0) "$neighbourMines" else state.mark
    }
}
