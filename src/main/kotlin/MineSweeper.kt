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
        var stat: Int
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
                val cell = gameBoard.getCell(x.toInt() - 1, y.toInt() - 1)
                if (action !in listOf("free", "mine")) {
                    throw IllegalArgumentException("Invalid action")
                }
                return Pair(cell, action)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (true)
    }

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
                        gameBoard.exploreNeighbours(cell)
                        1
                    } else {
                        gameBoard.showAllMines()
                        -1
                    }
                } else if (cell.state == CellState.EMPTY) {
                    gameBoard.exploreNeighbours(cell)
                    1
                } else 0
            }
            else -> {
                when (cell.state) {
                    CellState.EMPTY -> {
                        gameBoard.predict(cell)
                        1
                    }
                    CellState.MARKED -> {
                        gameBoard.predict(cell, false)
                        1
                    }
                    else -> 0
                }
            }
        }
    }
}