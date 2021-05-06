enum class CellState(val mark: String) {
    EMPTY(" "),
    MINE("X"),
    MARKED("*"),
    SAFE("/")
}