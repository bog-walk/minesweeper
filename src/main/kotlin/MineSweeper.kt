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
            val (cell, action) = parseMove()
            when (checkInput(cell, action, gameStep)) {
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

    // Creating cell using different coordinates based on drawn map, alter this in Gameboard
    private fun parseMove(): Pair<Cell, String> {
        do {
            print("Choose a cell to set/delete a mine or claim as free: ")
            val (x, y, action) = readLine()!!.trim().lowercase().split(Regex("\\s+"))
            try {
                if (x.toInt() !in (1..gameBoard.size) || y.toInt() !in (1..gameBoard.size)) {
                    throw IllegalArgumentException("Invalid cell coordinates")
                }
                if (action !in listOf("free", "mine")) {
                    throw IllegalArgumentException("Invalid action")
                }
                return Pair(Cell(x.toInt() - 1, y.toInt() - 1), action)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (true)
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

    private fun checkInput(cell: Cell, action: String, step: Int): Int {
        return when (action) {
            "free" -> {
                if (cell.isMine) {
                    if (step == 1) {
                        // ???????? Reset game?
                        do {
                            //minePositions.clear()
                            //generateMineField()
                        } while (cell.isMine)
                        gameBoard.exploreCellsRevised(cell)
                        1
                    } else {
                        gameBoard.showAllMines()
                        -1
                    }
                } else if (cell.state == CellState.EMPTY) {
                    gameBoard.exploreCellsRevised(cell)
                    1
                } else 0
            }
            else -> {
                when (cell.state) {
                    CellState.EMPTY -> {
                        cell.state = CellState.MARKED
                        gameBoard.predict(cell)
                        1
                    }
                    CellState.MARKED -> {
                        cell.state = CellState.EMPTY
                        gameBoard.predict(cell, false)
                        1
                    }
                    else -> 0
                }
            }
        }
    }
}