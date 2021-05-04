class MineSweeper {
    private val gameBoard: GameBoard

    init {
        val fieldSize = getBoardStat()
        // Should not be able to have every cell be a mine, right?
        val numOfMines = getBoardStat(fieldSize * fieldSize, true)
        gameBoard = GameBoard(fieldSize, numOfMines)
        play()
    }

    private fun getBoardStat(max: Int = 50, mines: Boolean = false): Int {
        var stat = 0
        print(if (!mines) {
            "How many rows do you want the field to take up? "
        } else {
            "How many mines do you want on the field? "
        })
        while (true) {
            try {
                stat = readLine()!!.toInt()
                if (stat in 1..max) {
                    break
                } else {
                    throw IllegalArgumentException()
                }
            } catch (e: NumberFormatException) {
                println("Please enter a number.")
            } catch (e: IllegalArgumentException) {
                println("Number must be between 1 and $max inclusive.")
            }
        }
        return stat
    }

    private fun play() {
        gameBoard.drawGameBoard()
        var gameOver = false
        var gameWon = false
        var gameStep = 0
        step@ do {
            gameStep++
            print("Set/delete mines marks or claim a cell as free: ")
            val (x, y, pred) = readLine()!!.trim().split(" ")
            //if (x.toInt() !in (1..fieldSize) || y.toInt() !in (1..fieldSize)) throw java.lang.IllegalArgumentException("Invalid coordinates")
            if (pred.toLowerCase() !in listOf("free", "mine")) throw java.lang.IllegalArgumentException("Invalid cell choice")
            when (checkInput(x.toInt() - 1, y.toInt() - 1, pred.toLowerCase(), gameStep)) {
                -1 -> {
                    gameOver = true
                    gameBoard.drawGameBoard()
                }
                1 -> gameBoard.drawGameBoard()
                else -> (continue@step)
            }
            gameWon = gameBoard.checkMinesMarked() || gameBoard.checkRemainingCells()
        } while (!gameOver && !gameWon)
        println(if (gameOver) "You stepped on a mine and failed!" else "Congratulations! You found all the mines.")
    }

    private fun parseMove() {

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
}