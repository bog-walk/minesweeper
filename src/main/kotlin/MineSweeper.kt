class MineSweeper(testMines: List<Int> = emptyList()) {
    private val gameBoard: GameBoard

    init {
        val fieldSize = getBoardStat()
        // Should not be able to have every cell be a mine, right?
        val numOfMines = getBoardStat(fieldSize * fieldSize, true)
        gameBoard = GameBoard(fieldSize, numOfMines, testMines)
    }

    // MineField max 9 rows & columns, rather than trying to draw with double
    // digit grid labels
    private fun getBoardStat(max: Int = 9, mines: Boolean = false): Int {
        println(if (!mines) {
            "How many rows do you want the field to take up?"
        } else {
            "How many mines do you want on the field?"
        })
        while (true) {
            try {
                val stat = readLine()!!.toInt()
                if (stat in 1..max) {
                    return stat
                } else {
                    throw IllegalArgumentException()
                }
            } catch (e: NumberFormatException) {
                println("Please enter a number.")
            } catch (e: IllegalArgumentException) {
                println("Number must be between 1 and $max inclusive.")
            }
        }
    }

    fun play() {
        println(gameBoard.drawGameBoard())
        var gameOver = false
        var gameWon = false
        step@ do {
            val (cell, action) = parseMove()
            when (checkInput(cell, action)) {
                -1 -> {
                    gameOver = true
                    println(gameBoard.drawGameBoard())
                }
                1 -> println(gameBoard.drawGameBoard())
                else -> continue@step
            }
            gameWon = gameBoard.checkMinesMarked() || gameBoard.checkRemainingCells()
            // Should this not be logical OR?
        } while (!gameOver && !gameWon)
        println(if (gameOver) "You stepped on a mine and failed!" else "Congratulations! You found all the mines.")
    }

    // Creating cell using different coordinates based on drawn map, alter this in Gameboard
    private fun parseMove(): Pair<Cell, String> {
        println("Choose a cell to mark/unkmark as a mine, or as safe.")
        println("Please use the format: row# column# mine|safe.")
        do {
            try {
                // Enter input as: # # action
                val (x, y, action) = readLine()!!.trim().lowercase().split(Regex("\\s+"))
                val cell = gameBoard.getCell(x.toInt() - 1, y.toInt() - 1)
                if (!isValidAction(action)) {
                    throw IllegalArgumentException("Action must be mine or safe.")
                }
                return Pair(cell, action)
            // Thrown if input line cannot be destructured into 3 parts
            // getCell() will also throw this, but how to differentiate?
            } catch (e: IndexOutOfBoundsException) {
                println("Please use the format: row# column# mine|safe.")
            } catch (e: NumberFormatException) {
                println("Please enter a number between 1 and ${gameBoard.size} inclusive.")
            // Also thrown by getCell() if IOOBExc caught internally
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (true)
    }

    private fun isValidAction(action: String): Boolean = action in listOf("safe", "mine")

    private fun checkInput(cell: Cell, action: String): Int {
        return when (action) {
            "safe" -> {
                // Player has stepped on mine
                if (cell.isMine) {
                    gameBoard.showAllMines()
                    -1
                // Removed when statement & 0 option as
                // exploreNeighbours() will not enter function block if
                // state not Empty or Marked.
                } else {
                    // Player has stepped on an unexplored cell or
                    // Player wants to change previous prediction
                    // i.e. not just unmark but declare cell safe
                    // Player has tried to step on a cell already explored
                    gameBoard.exploreNeighbours(cell)
                    1
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
                    // Safe cells cannot have state changed once made safe
                    else -> 0
                }
            }
        }
    }
}