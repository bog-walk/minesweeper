import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameBoardTest {
    @Test
    fun testInitialState_noMines() {
        val board = GameBoard(5, 0)
        assertEquals(5, board.size)
        val expected = " |12345|\n" +
                       "-|-----|\n" +
                       "1|     |\n" +
                       "2|     |\n" +
                       "3|     |\n" +
                       "4|     |\n" +
                       "5|     |\n" +
                       "-|-----|"
        assertEquals(expected, board.drawGameBoard())
        assertFalse(board.checkRemainingCells())
    }

    @Test
    fun testInitialState_allMines() {
        val board = GameBoard(5, 25)
        val expected = " |12345|\n" +
                "-|-----|\n" +
                "1|     |\n" +
                "2|     |\n" +
                "3|     |\n" +
                "4|     |\n" +
                "5|     |\n" +
                "-|-----|"
        assertEquals(expected, board.drawGameBoard())
        assertFalse(board.checkRemainingCells())
    }

    @Test
    fun testShowAllMines_allMines() {
        val board = GameBoard(5, 25)
        board.showAllMines()
        val expected = " |12345|\n" +
                "-|-----|\n" +
                "1|XXXXX|\n" +
                "2|XXXXX|\n" +
                "3|XXXXX|\n" +
                "4|XXXXX|\n" +
                "5|XXXXX|\n" +
                "-|-----|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testGetCell_cellExists() {
        val board = GameBoard(3, 0)
        val cell1 = board.getCell(0, 0)
        val cell2 = board.getCell(2, 2)
        assertEquals(0, cell1.xCoord)
        assertEquals(2, cell2.yCoord)
    }

    @Test
    fun testGetCell_cellInvalid() {
        val board = GameBoard(3, 0)
        assertThrows(IllegalArgumentException::class.java, {
            board.getCell(4, 5)
        }, "Invalid cell coordinates")
        assertThrows(IllegalArgumentException::class.java, {
            board.getCell(-1, 1)
        }, "Invalid cell coordinates")
    }

    @Test
    fun testPredict_addAllMines() {
        val board = GameBoard(3, 9)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cell = board.getCell(i, j)
                board.predict(cell, true)
            }
        }
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|***|\n" +
                "2|***|\n" +
                "3|***|\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
        // all mines should be marked as board filled with mines
        assertTrue(board.checkMinesMarked())
        // no cells left to predict & all marked correctly
        assertTrue(board.checkRemainingCells())
    }

    @Test
    fun testPredict_removeMines() {
        val board = GameBoard(3, 9)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cell = board.getCell(i, j)
                board.predict(cell, true)
            }
        }
        board.predict(board.getCell(0, 0), false)
        board.predict(board.getCell(1, 1), false)
        board.predict(board.getCell(2, 2), false)
        val expected = " |123|\n" +
                "-|---|\n" +
                "1| **|\n" +
                "2|* *|\n" +
                "3|** |\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
        assertFalse(board.checkMinesMarked())
        assertFalse(board.checkRemainingCells())
    }

    @Test
    fun testExploreNeighbours_oneMine() {
        val board = GameBoard(3, 1)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                board.getCell(i, j).apply {
                    this.isMine = i == 1 && j == i
                }
            }
        }
        board.exploreNeighbours(board.getCell(1, 1))
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|///|\n" +
                "2|///|\n" +
                "3|///|\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_allMines() {
        val board = GameBoard(3, 9)
        board.exploreNeighbours(board.getCell(1, 1))
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2|   |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_allMinesExceptOne() {
        val board = GameBoard(3, 9)
        val emptyCell = board.getCell(1, 1).apply {
            this.isMine = false
        }
        board.exploreNeighbours(emptyCell)
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2| 8 |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_twoMines() {
        val board = GameBoard(3, 2)
        board.exploreNeighbours(board.getCell(1, 1))
        board.showAllMines()
        println(board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_largeBoard_fewMines() {
        val board = GameBoard(9, 1)
        board.exploreNeighbours(board.getCell(2, 2))
        board.showAllMines()
        println(board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_largeBoard_manyMines() {
        val board = GameBoard(9, 50)
        board.exploreNeighbours(board.getCell(2, 2))
        board.showAllMines()
        println(board.drawGameBoard())
    }
}