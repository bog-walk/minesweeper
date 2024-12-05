package dev.bogwalk.model

import androidx.compose.runtime.Immutable
import org.jetbrains.annotations.TestOnly

enum class CellState {
    UNSELECTED, SELECTED, FLAGGED
}

@Immutable
data class Cell(
    val coordinates: Pair<Int, Int>,
    val state: CellState = CellState.UNSELECTED,
    val neighbourMines: Int = 0
) {
    val isMine: Boolean
        get() = neighbourMines == -1

    @TestOnly
    override fun toString(): String {
        return when (state) {
            CellState.UNSELECTED -> " "
            CellState.FLAGGED -> "?"
            CellState.SELECTED -> if (isMine) "X" else neighbourMines.toString()
        }
    }
}