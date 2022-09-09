package dev.bogwalk.model

enum class CellState {
    UNSELECTED, SELECTED, FLAGGED
}

enum class GameState {
    PLAYING, WON, LOST
}

enum class DigitSegment(private val cache: Byte) {
    ZERO(0b1111011),
    ONE(0b0010010),
    TWO(0b1100111),
    THREE(0b0110111),
    FOUR(0b0011110),
    FIVE(0b0111101),
    SIX(0b1111101),
    SEVEN(0b0010011),
    EIGHT(0b1111111),
    NINE(0b0111111);

    fun isActive(pathIndex: Int): Boolean = (cache.toInt() shr pathIndex) and 1 == 1

    companion object {
        fun fromDigit(digit: Int): DigitSegment = values()[digit]
    }
}