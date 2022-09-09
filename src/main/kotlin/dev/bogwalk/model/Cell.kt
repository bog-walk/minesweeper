package dev.bogwalk.model

import androidx.compose.runtime.Immutable

@Immutable
data class Cell(
    val coordinates: Pair<Int, Int>,
    val state: CellState = CellState.UNSELECTED,
    val neighbourMines: Int = 0
) {
    val isMine: Boolean
        get() = neighbourMines == -1

    override fun toString(): String {
        return when (state) {
            CellState.UNSELECTED -> " "
            CellState.FLAGGED -> "?"
            CellState.SELECTED -> if (isMine) "X" else neighbourMines.toString()
        }
    }
}