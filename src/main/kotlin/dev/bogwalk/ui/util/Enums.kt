package dev.bogwalk.ui.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bogwalk.ui.style.cellSize

enum class GameState {
    PLAYING, WON, LOST
}

enum class Level(val values: List<Int>) {
    BEGINNER(listOf(9, 9, 10)),
    INTERMEDIATE(listOf(16, 16, 40)),
    EXPERT(listOf(16, 30, 99));

    fun windowWidth(): Dp = calcWindowWidth(values[1])

    fun windowHeight(): Dp = calcWindowHeight(values[0])
}

fun calcWindowWidth(columns: Int) = (columns * cellSize.value).dp + 46.dp

fun calcWindowHeight(rows: Int) = (rows * cellSize.value).dp + 157.dp