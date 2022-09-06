package dev.bogwalk.model

enum class CellState(val mark: String) {
    EMPTY(" "),
    MINE("X"),
    MARKED("*"),
    SAFE("/")
}