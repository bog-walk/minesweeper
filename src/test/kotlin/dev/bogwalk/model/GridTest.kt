package dev.bogwalk.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class GridTest {
    @Test
    fun `test initial load state with no mines`() {
        val board = TestGrid(emptyList())
        assertEquals(0, board.flagsRemaining)
        val expected = " |12345|\n" +
                       "-|-----|\n" +
                       "1|     |\n" +
                       "2|     |\n" +
                       "3|     |\n" +
                       "4|     |\n" +
                       "5|     |\n" +
                       "-|-----|"
        assertEquals(expected, board.drawBoard())
        val expectedCount = " |12345|\n" +
                "-|-----|\n" +
                "1|00000|\n" +
                "2|00000|\n" +
                "3|00000|\n" +
                "4|00000|\n" +
                "5|00000|\n" +
                "-|-----|"
        assertEquals(expectedCount, board.drawBoard(true))
    }

    @Test
    fun `test initial load state all mines`() {
        val mines = List(6) { it / 3 to it % 3 }
        val board = TestGrid(mines, 2, 3)
        assertEquals(6, board.flagsRemaining)
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawBoard())
        val expectedCount = " |123|\n" +
                "-|---|\n" +
                "1|XXX|\n" +
                "2|XXX|\n" +
                "-|---|"
        assertEquals(expectedCount, board.drawBoard(true))
    }

    @Test
    fun `test initial load state with some mines`() {
        val mines = listOf(0 to 0, 2 to 1, 2 to 3, 4 to 1)
        val board = TestGrid(mines, 5, 4)
        assertEquals(4, board.flagsRemaining)
        val expected = " |1234|\n" +
                "-|----|\n" +
                "1|    |\n" +
                "2|    |\n" +
                "3|    |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected, board.drawBoard())
        val expectedCount = " |1234|\n" +
                "-|----|\n" +
                "1|X100|\n" +
                "2|2221|\n" +
                "3|1X2X|\n" +
                "4|2231|\n" +
                "5|1X10|\n" +
                "-|----|"
        assertEquals(expectedCount, board.drawBoard(true))
    }

    @Test
    fun `flagCell() correct with valid input`() {
        val mines = listOf(0 to 0, 2 to 1, 2 to 3, 4 to 1)
        val board = TestGrid(mines, 5, 4)
        assertEquals(4, board.flagsRemaining)

        board.flagCell(mines[0])
        assertEquals(3, board.flagsRemaining)
        val expected1 = " |1234|\n" +
                "-|----|\n" +
                "1|?   |\n" +
                "2|    |\n" +
                "3|    |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected1, board.drawBoard())

        board.flagCell(mines[2])
        assertEquals(2, board.flagsRemaining)
        val expected2 = " |1234|\n" +
                "-|----|\n" +
                "1|?   |\n" +
                "2|    |\n" +
                "3|   ?|\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected2, board.drawBoard())

        board.flagCell(mines[0])
        assertEquals(3, board.flagsRemaining)
        val expected3 = " |1234|\n" +
                "-|----|\n" +
                "1|    |\n" +
                "2|    |\n" +
                "3|   ?|\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected3, board.drawBoard())
    }

    @Test
    fun `flagCell() limits amount of flags`() {
        val mines = listOf(0 to 0)
        val board = TestGrid(mines, 5, 4)
        assertEquals(1, board.flagsRemaining)

        board.flagCell(mines[0])
        assertEquals(0, board.flagsRemaining)
        val expected1 = " |1234|\n" +
                "-|----|\n" +
                "1|?   |\n" +
                "2|    |\n" +
                "3|    |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected1, board.drawBoard())

        board.flagCell(1 to 1)
        assertEquals(0, board.flagsRemaining)
        val expected2 = " |1234|\n" +
                "-|----|\n" +
                "1|?   |\n" +
                "2|    |\n" +
                "3|    |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected2, board.drawBoard())
    }

    @Test
    fun `selectCell() correct with valid input`() {
        val mines = listOf(0 to 0, 2 to 1, 4 to 0)
        val board = TestGrid(mines, 5, 4)
        assertEquals(3, board.flagsRemaining)

        board.flagCell(mines[1])
        assertEquals(2, board.flagsRemaining)
        val expected1 = " |1234|\n" +
                "-|----|\n" +
                "1|    |\n" +
                "2|    |\n" +
                "3| ?  |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected1, board.drawBoard())

        assertTrue { board.selectCell(mines[1]) }
        assertEquals(2, board.flagsRemaining)
        assertEquals(expected1, board.drawBoard())

        assertTrue { board.selectCell(0 to 1) }
        assertEquals(2, board.flagsRemaining)
        val expected2 = " |1234|\n" +
                "-|----|\n" +
                "1| 1  |\n" +
                "2|    |\n" +
                "3| ?  |\n" +
                "4|    |\n" +
                "5|    |\n" +
                "-|----|"
        assertEquals(expected2, board.drawBoard())

        assertTrue { board.selectCell(4 to 3) }
        assertEquals(2, board.flagsRemaining)
        val expected3 = " |1234|\n" +
                "-|----|\n" +
                "1| 100|\n" +
                "2| 210|\n" +
                "3| ?10|\n" +
                "4| 210|\n" +
                "5| 100|\n" +
                "-|----|"
        assertEquals(expected3, board.drawBoard())

        assertTrue { board.selectCell(3 to 0) }
        assertEquals(2, board.flagsRemaining)
        val expected4 = " |1234|\n" +
                "-|----|\n" +
                "1| 100|\n" +
                "2| 210|\n" +
                "3| ?10|\n" +
                "4|2210|\n" +
                "5| 100|\n" +
                "-|----|"
        assertEquals(expected4, board.drawBoard())

        board.flagCell(mines[0])
        board.flagCell(mines[2])
        assertEquals(0, board.flagsRemaining)
        assertFalse { board.allMinesFound }
        val expected5 = " |1234|\n" +
                "-|----|\n" +
                "1|?100|\n" +
                "2| 210|\n" +
                "3| ?10|\n" +
                "4|2210|\n" +
                "5|?100|\n" +
                "-|----|"
        assertEquals(expected5, board.drawBoard())

        assertTrue { board.selectCell(1 to 0) }
        assertFalse { board.allMinesFound }
        assertTrue { board.selectCell(2 to 0) }
        assertTrue { board.allMinesFound }
    }

    @Test
    fun `flagCell() and selectCell() throw with invalid input`() {
        val mines = listOf(0 to 0, 2 to 1, 4 to 1)
        val board = TestGrid(mines, 5, 4)
        assertEquals(3, board.flagsRemaining)

        assertTrue { board.selectCell(0 to 1) }
        assertFailsWith<IllegalArgumentException> { board.flagCell(0 to 1) }
        assertFailsWith<IllegalArgumentException> { board.selectCell(0 to 1) }
    }

    @Test
    fun `selectCell() picks a mine`() {
        val mines = listOf(1 to 1)
        val board = TestGrid(mines, 3, 3)
        assertFalse { board.selectCell(mines[0]) }
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2| X |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawBoard())
    }

    @Test
    fun `selectCell() picks the only non-mine`() {
        val mines = listOf(
            0 to 0, 0 to 1, 0 to 2,
            1 to 0, 1 to 2,
            2 to 0, 2 to 1, 2 to 2
        )
        val board = TestGrid(mines, 3, 3)
        assertTrue { board.selectCell(1 to 1) }
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2| 8 |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawBoard())
    }

    @Test
    fun `selectCell() expands selection if no near mines`() {
        val mines = listOf(1 to 2, 2 to 1, 2 to 2)
        val board = TestGrid(mines, 3, 3)
        assertTrue { board.selectCell(0 to 0) }
        val expected = " |123|\n" +
                "-|---|\n" +
                "1|01 |\n" +
                "2|13 |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected, board.drawBoard())
    }

    @Test
    fun `selectCell() expands selection if few mines`() {
        val mines = listOf(0 to 1, 1 to 4)
        val board = TestGrid(mines, 9, 9)
        assertTrue { board.selectCell(8 to 5) }
        val expected = " |123456789|\n" +
                "-|---------|\n" +
                "1|     1000|\n" +
                "2|1111 1000|\n" +
                "3|000111000|\n" +
                "4|000000000|\n" +
                "5|000000000|\n" +
                "6|000000000|\n" +
                "7|000000000|\n" +
                "8|000000000|\n" +
                "9|000000000|\n" +
                "-|---------|"
        assertEquals(expected, board.drawBoard())
    }

    @Test
    fun `selectCell() wins game with single click`() {
        val mines = listOf(3 to 0, 2 to 4)
        val board = TestGrid(mines, 6, 5)
        assertEquals(2, board.flagsRemaining)

        assertTrue { board.selectCell(0 to 0) }
        assertEquals(0, board.flagsRemaining)
        assertTrue { board.allMinesFound }
        val expected = " |12345|\n" +
                "-|-----|\n" +
                "1|00000|\n" +
                "2|00011|\n" +
                "3|1101 |\n" +
                "4| 1011|\n" +
                "5|11000|\n" +
                "6|00000|\n" +
                "-|-----|"
        assertEquals(expected, board.drawBoard())
    }

    @Test
    fun `reset() works correctly`() {
        val mines = listOf(
            0 to 0, 0 to 1, 0 to 2,
            1 to 0, 1 to 2,
            2 to 0, 2 to 1, 2 to 2
        )
        val board = TestGrid(mines, 3, 3)
        assertTrue { board.selectCell(1 to 1) }
        assertTrue { board.allMinesFound }
        val expected1 = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2| 8 |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected1, board.drawBoard())
        board.reset()

        assertEquals(8, board.flagsRemaining)
        assertFalse { board.allMinesFound }
        val expected2 = " |123|\n" +
                "-|---|\n" +
                "1|   |\n" +
                "2|   |\n" +
                "3|   |\n" +
                "-|---|"
        assertEquals(expected2, board.drawBoard())
    }
}