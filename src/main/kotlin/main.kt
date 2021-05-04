enum class CellState(val mark: String) {
    EMPTY("."), MINE("X"), MARKED("*"), SAFE("/")
}

class MineSweeper(private val fieldSize: Int, private val numMines: Int) {
    private val playingField = Array(fieldSize) { Array(fieldSize) { CellState.EMPTY.mark } }
    private val predictedMines = mutableListOf<Pair<Int, Int>>()
    private var minePositions = mutableListOf<Pair<Int, Int>>()

    init {
        generateMineField()
    }

    private fun generateMineField() {
        val mines: List<Int> = (0 until fieldSize * fieldSize).shuffled().take(numMines)
        for (i in 0 until numMines) {
            val row = mines[i] / fieldSize
            val col = mines[i] % fieldSize
            minePositions.add(Pair(row, col))
        }
    }

//    private fun countNeighbourMines(row: Int, col: Int): Int {
//        val n = listOf(-1, 0, 1)
//        val x = n.map { it + col }
//        val y = n.map { it + row }
//        val neighbours = mutableListOf<Pair<Int, Int>>()
//        for (i in y) {
//            for (j in x) {
//                if (i == row && j == col) continue
//                neighbours.add(Pair(i, j))
//            }
//        }
//        return neighbours.count { it in minePositions }
//    }

// Note that 2 uncommented methods were not generating counts/hints for the occasional diagonal cell, leaving empty

//    private fun exploreCells(row: Int, col: Int) {
//        while (playingField[row][col] == CellState.EMPTY.mark || playingField[row][col] == CellState.MARKED.mark) {
//            if (Pair(row, col) in minePositions) {
//                break
//            } else {
//                val nearbyMines = countNeighbourMines(row, col)
//                playingField[row][col] = if (nearbyMines > 0) nearbyMines.toString() else CellState.SAFE.mark
//                val newRow = (row + 1) % fieldSize
//                val newCol = (col + 1) % fieldSize
//                exploreCells(newRow, col)
//                exploreCells(row, newCol)
//            }
//        }
//    }

    private fun exploreCellsRevised(row: Int, col: Int) {
        while (playingField[row][col] == CellState.EMPTY.mark || playingField[row][col] == CellState.MARKED.mark) {
            val n = listOf(-1, 0, 1)
            val x = n.map { it + col }
            val y = n.map { it + row }
            val neighbours = mutableListOf<Pair<Int, Int>>()
            for (i in y) {
                for (j in x) {
                    if (i == row && j == col) continue
                    neighbours.add(Pair(i, j))
                }
            }
            val count = neighbours.count { it in minePositions }
            playingField[row][col] = if (count > 0) count.toString() else "/"
            for (pair in neighbours) {
                val y = pair.first
                val x = pair.second
                if (y in 0 until fieldSize && x in 0 until fieldSize
                    && Pair(y, x) !in minePositions && playingField[y][x] != CellState.SAFE.mark) {
                    exploreCellsRevised(y, x)
                }
            }
        }
    }

    fun drawMineField() {
        println(" |${(1..fieldSize).joinToString("")}|")
        println("-|${"-".repeat(fieldSize)}|")
        for (i in 0 until fieldSize) {
            print("${i + 1}|")
            for (j in 0 until fieldSize) {
                print(playingField[i][j])
            }
            print("|\n")
        }
        println("-|${"-".repeat(fieldSize)}|")
    }

    fun checkInput(x: Int, y: Int, pred: String, step: Int): Int {
        return when (pred) {
            "free" -> {
                if (Pair(y, x) in minePositions) {
                    if (step == 1) {
                        do {
                            minePositions.clear()
                            generateMineField()
                        } while (Pair(y, x) in minePositions)
                        exploreCellsRevised(y, x)
                        1
                    } else {
                        for (coords in minePositions) {
                            playingField[coords.first][coords.second] = CellState.MINE.mark
                        }
                        -1
                    }
                } else if (playingField[y][x] == CellState.EMPTY.mark) {
                    exploreCellsRevised(y, x)
                    1
                } else 0
            }
            else -> {
                when (playingField[y][x]) {
                    CellState.EMPTY.mark -> {
                        playingField[y][x] = CellState.MARKED.mark
                        predictedMines.add(Pair(y, x))
                        1
                    }
                    CellState.MARKED.mark -> {
                        playingField[y][x] = CellState.EMPTY.mark
                        predictedMines.remove(Pair(y, x))
                        1
                    }
                    else -> 0
                }
            }
        }
    }

    fun checkMinesMarked(): Boolean {
        return predictedMines.isNotEmpty() &&
                predictedMines.size == minePositions.size &&
                predictedMines.toTypedArray().contentDeepEquals(minePositions.toTypedArray())
    }

    fun checkRemainingCells(): Boolean {
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                if ((playingField[i][j] == CellState.EMPTY.mark || playingField[i][j] == CellState.MARKED.mark) &&
                    Pair(i, j) !in minePositions) return false
            }
        }
        return true
    }
}

fun main() {
    print("How many rows do you want the field to take up? ")
    val fieldSize = readLine()!!.toInt()
    print("How many mines do you want on the field? ")
    val numMines = readLine()!!.toInt()
    if (numMines > fieldSize * fieldSize) throw IllegalArgumentException("Too many mines")
    val game = MineSweeper(fieldSize, numMines)
    game.drawMineField()
    var gameOver: Boolean = false
    var gameWon: Boolean = false
    var gameStep: Int = 0
    step@ do {
        gameStep++
        print("Set/delete mines marks or claim a cell as free: ")
        val (x, y, pred) = readLine()!!.trim().split(" ")
        if (x.toInt() !in (1..fieldSize) || y.toInt() !in (1..fieldSize)) throw java.lang.IllegalArgumentException("Invalid coordinates")
        if (pred.toLowerCase() !in listOf("free", "mine")) throw java.lang.IllegalArgumentException("Invalid cell choice")
        when (game.checkInput(x.toInt() - 1, y.toInt() - 1, pred.toLowerCase(), gameStep)) {
            -1 -> {
                gameOver = true
                game.drawMineField()
            }
            1 -> game.drawMineField()
            else -> (continue@step)
        }
        gameWon = game.checkMinesMarked() || game.checkRemainingCells()
    } while (!gameOver && !gameWon)
    println(if (gameOver) "You stepped on a mine and failed!" else "Congratulations! You found all the mines.")
}