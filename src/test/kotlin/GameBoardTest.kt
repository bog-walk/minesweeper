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
}