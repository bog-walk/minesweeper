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