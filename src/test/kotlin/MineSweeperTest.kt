import org.junit.jupiter.api.Test
import java.io.*
import kotlin.test.assertEquals

internal class MineSweeperTest {
    @Test
    fun testPlay_loseInOneMove() {
        val userInput = "3${System.lineSeparator()}"+
                "9${System.lineSeparator()}" +
                "1 1 safe${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        MineSweeper().play()
        val expected = File("src/test/resources/simpleBoard_allMines_Lose")
            .bufferedReader()
            .readLines()
        val actual = output.toString().trim().split(System.lineSeparator())
        assertEquals(expected, actual)
    }

    @Test
    fun testPlay_winByMarkingAll() {
        val userInput = "2${System.lineSeparator()}"+
                "4${System.lineSeparator()}" +
                "1 1 mine${System.lineSeparator()}" +
                "1 2 mine${System.lineSeparator()}" +
                "2 1 mine${System.lineSeparator()}" +
                "2 2 mine${System.lineSeparator()}"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        MineSweeper().play()
        val expected = File("src/test/resources/simpleBoard_allMines_Win")
            .bufferedReader()
            .readLines()
        val actual = output.toString().trim().split(System.lineSeparator())
        assertEquals(expected, actual)
    }
}