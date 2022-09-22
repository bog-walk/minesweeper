package dev.bogwalk.ui.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GameState {
    PLAYING, WON, LOST
}

enum class Level(val values: List<Int>, val size: Pair<Dp, Dp>) {
    BEGINNER(listOf(9, 9, 10), 226.dp to 337.dp),
    INTERMEDIATE(listOf(16, 16, 40), 366.dp to 477.dp),
    EXPERT(listOf(16, 30, 99), 646.dp to 477.dp);

    //override fun toString(): String {
        //return "${name.padEnd(15)} ${values[0]} by ${values[1]}   ${values[2]} mines"
    //}
}