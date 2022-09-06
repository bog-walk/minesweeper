import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CellTest {
    @Test
    fun testInitialState() {
        val cell = Cell(0, 0)
        assertEquals(CellState.EMPTY, cell.state)
        assertEquals(0, cell.neighbourMines)
        assertFalse(cell.isMine)
        assertFalse(cell.foundMine)
    }

    @Test
    fun testToString() {
        val cellEmpty = Cell(1, 1)
        val cellMine = Cell(10, 10).apply {
            this.state = CellState.MINE
        }
        val cellSurrounded = Cell(3, 18).apply {
            this.neighbourMines = 4
        }
        assertEquals(" ", cellEmpty.toString())
        assertEquals("X", cellMine.toString())
        assertEquals("4", cellSurrounded.toString())
    }

    @Test
    fun testEquals() {
        val cell1 = Cell(1, 1)
        val cell2 = Cell(2, 2)
        val cell3 = Cell(1, 1).apply {
            this.state = CellState.MINE
            this.isMine = true
        }
        assertFalse(cell1 == cell2)
        assertFalse(cell2 == cell3)
        assertTrue(cell1 == cell3)
    }

    @Test
    fun testFoundMine() {
        val cellNotFound = Cell(1, 1).apply {
            this.isMine = true
        }
        val cellFound = Cell(2, 2).apply {
            this.isMine = true
            this.state = CellState.MARKED
        }
        assertFalse(cellNotFound.foundMine)
        assertTrue(cellFound.foundMine)
    }
}