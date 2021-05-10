import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * These tests only work if the MineSweeper methods are
 * made public, which is bad practice.
 * Note that project currently not running on WSL2 so cannot test
 * in CLI.
 */
internal class MineSweeperTestBad {
    @Test
    fun testGetBoardStat_validFieldSize() {
        val userInput = "20"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val game = MineSweeper()
        //assertEquals(20, game.getBoardStat())
    }

    @Test
    fun testGetBoardStat_notANumber() {
        // Reassign standard in to take a type of InputStream
        val userInput = "AAA${System.lineSeparator()}3${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        // Reassign standard out to take a type of PrintStream
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        val game = MineSweeper()
        // Call the function to be tested as will loop rather than throw Exception
        //game.getBoardStat()
        val output = baos.toString().trim().split(System.lineSeparator())
        // Last line printed to console will show thrown Exception message
        assertEquals("Please enter a number.", output.last())
    }

    @Test
    fun testGetBoardStat_invalidFieldSize() {
        val userInput = "100${System.lineSeparator()}3${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        val game = MineSweeper()
        //game.getBoardStat()
        val output = baos.toString().trim().split(System.lineSeparator())
        val expected = "Number must be between 1 and 50 inclusive."
        assertEquals(expected, output.last())
    }

    @Test
    fun testGetBoardStat_validMines() {
        val userInput = "2"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val game = MineSweeper()
        //assertEquals(2, game.getBoardStat(10, mines = true))
    }

    @Test
    fun testParseMove_validMove() {
        val userInput = "1 1 mine"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val game = MineSweeper()
        //val returned = game.parseMove()
        //assertEquals(0, returned.first.xCoord)
        //assertEquals("mine", returned.second)
    }

    @Test
    fun testParseMove_invalidCoordinates() {
        val userInput = "x y mine${System.lineSeparator()}" +
                "100 100 mine${System.lineSeparator()}" +
                "1 1 mine${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        val game = MineSweeper()
        //game.parseMove()
        val output = baos.toString().trim().split(System.lineSeparator())
        val expected = "Cell coordinates must be between 1 and 3 inclusive."
        // First exception thrown as coords not numbers
        assertEquals(expected, output[output.lastIndex - 1])
        // Second exception thrown as coords out of bounds
        assertEquals(expected, output.last())
    }

    @Test
    fun testParseMove_invalidAction() {
        val userInput = "1 1 exit${System.lineSeparator()}" +
                "1 1 safe${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        val game = MineSweeper()
        //game.parseMove()
        val output = baos.toString().trim().split(System.lineSeparator())
        val expected = "Action must be mine or safe."
        assertEquals(expected, output.last())
    }

}