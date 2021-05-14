import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameBoardTest {
    @Test
    fun testInitialState_noMines() {
        val board = GameBoard(5, 0, emptyList())
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
        val board = GameBoard(5, 25, emptyList())
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
        val board = GameBoard(5, 25, emptyList())
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
        val board = GameBoard(3, 0, emptyList())
        val cell1 = board.getCell(0, 0)
        val cell2 = board.getCell(2, 2)
        assertEquals(0, cell1.xCoord)
        assertEquals(2, cell2.yCoord)
    }

    @Test
    fun testGetCell_cellInvalid() {
        val board = GameBoard(3, 0, emptyList())
        assertThrows(IllegalArgumentException::class.java, {
            board.getCell(4, 5)
        }, "Invalid cell coordinates")
        assertThrows(IllegalArgumentException::class.java, {
            board.getCell(-1, 1)
        }, "Invalid cell coordinates")
    }

    @Test
    fun testPredict_addAllMines() {
        val board = GameBoard(3, 9, emptyList())
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
        val board = GameBoard(3, 9, emptyList())
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
    fun testExploreNeighbours_oneMine_isChosen() {
        val board = GameBoard(3, 1, listOf(4))
        board.exploreNeighbours(board.getCell(1, 1))
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|111|\n" +
                "2|1 1|\n" +
                "3|111|\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_oneMine_notChosen() {
        val board = GameBoard(3, 1, listOf(4))
        board.exploreNeighbours(board.getCell(0, 0))
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|111|\n" +
                "2|1 1|\n" +
                "3|111|\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_allMines() {
        val board = GameBoard(3, 9, emptyList())
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
    fun testExploreNeighbours_allMinesExceptOne_isChosen() {
        val board = GameBoard(3, 9, emptyList())
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
    fun testExploreNeighbours_threeMines() {
        val board = GameBoard(3, 3, listOf(5, 7, 8))
        board.exploreNeighbours(board.getCell(0, 0))
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|/11|\n" +
                "2|13 |\n" +
                "3|1  |\n" +
                "-|---|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_largeBoard_fewMines() {
        val board = GameBoard(9, 2, listOf(1, 13))
        board.exploreNeighbours(board.getCell(8, 5))
        val expected = " |123456789|\n" +
                "-|---------|\n" +
                "1|1 1111///|\n" +
                "2|1111 1///|\n" +
                "3|///111///|\n" +
                "4|/////////|\n" +
                "5|/////////|\n" +
                "6|/////////|\n" +
                "7|/////////|\n" +
                "8|/////////|\n" +
                "9|/////////|\n" +
                "-|---------|"
        assertEquals(expected, board.drawGameBoard())
    }

    @Test
    fun testExploreNeighbours_largeBoard_manyMines() {
        val mines = listOf(1, 2, 5, 6, 9, 10, 15, 18, 21, 23, 24, 32,
            33, 34, 37, 38, 41, 42, 43, 44, 46, 48, 49, 50, 51, 59, 63,
            64, 65, 68, 70, 72, 74, 76, 77)
        val board = GameBoard(9, 35, mines)
        board.exploreNeighbours(board.getCell(4, 3))
        val expected = " |123456789|\n" +
                "-|---------|\n" +
                "1|   11    |\n" +
                "2|  4235   |\n" +
                "3| 32 3    |\n" +
                "4|23324    |\n" +
                "5|2  35    |\n" +
                "6|2 4      |\n" +
                "7|34435    |\n" +
                "8|   34    |\n" +
                "9|   3     |\n" +
                "-|---------|"
        assertEquals(expected, board.drawGameBoard())
    }
}