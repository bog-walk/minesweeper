class MineSweeper {
    private val playingField = PlayingField(fieldSize, numMines)

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