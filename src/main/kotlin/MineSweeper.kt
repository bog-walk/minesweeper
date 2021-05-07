class MineSweeper {
    private val gameBoard: GameBoard

    init {
        val fieldSize = getBoardStat()
        // Should not be able to have every cell be a mine, right?
        val numOfMines = getBoardStat(fieldSize * fieldSize, true)
        gameBoard = GameBoard(fieldSize, numOfMines)
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

    fun play() {
        gameBoard.drawGameBoard()
        var gameOver = false
        var gameWon = false
        var gameStep = 0
        step@ do {
            // What is the point of counting steps?
            gameStep++
            val (cell, action) = parseMove()
            when (checkInput(cell, action, gameStep)) {
                -1 -> {
                    gameOver = true
                    gameBoard.drawGameBoard()
                }
                1 -> gameBoard.drawGameBoard()
                else -> continue@step
            }
            gameWon = gameBoard.checkMinesMarked() || gameBoard.checkRemainingCells()
            // Should this not be logical OR?
        } while (!gameOver && !gameWon)
        println(if (gameOver) "You stepped on a mine and failed!" else "Congratulations! You found all the mines.")
    }

    // Creating cell using different coordinates based on drawn map, alter this in Gameboard
    private fun parseMove(): Pair<Cell, String> {
        do {
            print("Choose a cell to claim/unmark as a mine or as safe: ")
            val (x, y, action) = readLine()!!.trim().lowercase().split(Regex("\\s+"))
            try {
                val cell = gameBoard.getCell(x.toInt() - 1, y.toInt() - 1)
                if (action !in listOf("safe", "mine")) {
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
            "safe" -> {
                // Player has stepped on mine
                if (cell.isMine) {
                    gameBoard.showAllMines()
                    -1
                } else when (cell.state) {
                    // Player has stepped on an unexplored cell
                    CellState.EMPTY -> {
                        gameBoard.exploreNeighbours(cell)
                        1
                    }
                    // Player wants to change prediction
                    CellState.MARKED -> {
                        gameBoard.predict(cell, false)
                        1
                    }
                    // Player has tried to step on a cell already explored
                    else -> 0
                }
            }
            else -> {
                when (cell.state) {
                    // Player predicts an unexplored cell as having a mine
                    CellState.EMPTY -> {
                        gameBoard.predict(cell)
                        1
                    }
                    // Player predicts previously marked cell as not being a mine
                    // Will not set it as being safe though
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