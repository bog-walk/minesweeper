package dev.bogwalk.ui.integration

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.model.TestGrid
import dev.bogwalk.ui.MinesweeperApp
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.MinesweeperAppState
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MinesweeperAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `MinesweeperApp() correct for single-click win`() {
        val testGrid = TestGrid(listOf(6 to 0, 6 to 5, 6 to 6), 9, 9)
        val testState = MinesweeperAppState(testGrid)

        composeTestRule.setContent {
            MinesweeperApp(testState)
        }

        // initial set up as expected
        composeTestRule.onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION).assertExists()
        assertEquals(3, testState.flagsRemaining)
        assertEquals(0, testState.timer.seconds)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertCountEquals(81).assertAll(isEnabled())

        // select first cell to clear all but mines for instant win :)
        composeTestRule.onNodeWithTag("(0,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_WON_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertAll(isNotEnabled())
            .filter(hasTextExactly("1")).assertCountEquals(11)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(hasTextExactly("2")).assertCountEquals(4)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(3)
        assertEquals(0, testState.flagsRemaining)
    }

    // Unable to test right click due to NotImplementedErrors from test module
    //@OptIn(ExperimentalTestApi::class)
    @Test
    fun `MinesweeperAppState() works correctly`() {
        val testGrid = TestGrid(listOf(0 to 0, 2 to 1, 4 to 0), 5, 5)
        val testState = MinesweeperAppState(testGrid)

        composeTestRule.setContent {
            MinesweeperApp(testState)
        }

        // initial set up as expected
        composeTestRule.onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION).assertExists()
        assertEquals(3, testState.flagsRemaining)
        assertEquals(0, testState.timer.seconds)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertCountEquals(25).assertAll(isEnabled())

        // select first cell
        composeTestRule.onNodeWithTag("(4,4)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(isNotEnabled()).assertCountEquals(19)
            .filter(hasTextExactly("1")).assertCountEquals(5)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(hasTextExactly("2")).assertCountEquals(2)

        // flag a mine
        // androidx.compose.ui.test.DesktopInputDispatcher riddled with TODOs causing NotImplementedErrors...
        //composeTestRule.onNodeWithTag("(2,1)").performMouseInput { rightClick() }
        //composeTestRule.waitForIdle()

        //assertEquals(2, testState.flagsRemaining)
        //composeTestRule.onNodeWithTag("(2,1)").assert(hasContentDescription(FLAG_DESCRIPTION))

        // select a mine :(
        composeTestRule.onNodeWithTag("(0,0)").performClick()
        composeTestRule.waitForIdle()

        //assertEquals(2, testState.flagsRemaining)
        composeTestRule.onNodeWithContentDescription(RESET_LOST_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertAll(isNotEnabled())
            .filter(hasContentDescription(MINE_DESCRIPTION)).assertCountEquals(3)

        // reset board
        composeTestRule.onNodeWithContentDescription(RESET_LOST_DESCRIPTION).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION).assertExists()
        assertEquals(3, testState.flagsRemaining)
        assertEquals(0, testState.timer.seconds)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertCountEquals(25).assertAll(isEnabled())

        // select first cell
        composeTestRule.onNodeWithTag("(2,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("(2,0)").assertIsNotEnabled().assertTextEquals("1")
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(isEnabled()).assertCountEquals(24)

        // flag 2 mines
        //composeTestRule.onNodeWithTag("(2,1)").performMouseInput { rightClick() }
        //composeTestRule.waitForIdle()
        //composeTestRule.onNodeWithTag("(0,0)").performMouseInput { rightClick() }
        //composeTestRule.waitForIdle()

        //assertEquals(1, testState.flagsRemaining)
        //composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            //.filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(2)

        // select second cell to clear most of grid
        composeTestRule.onNodeWithTag("(0,2)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(isNotEnabled()).assertCountEquals(20)
            .filter(hasTextExactly("1")).assertCountEquals(6)

        // select last 2 non-mine cells to win game :)
        composeTestRule.onNodeWithTag("(1,0)").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("(3,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_WON_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertAll(isNotEnabled())
            .filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(3)
        assertEquals(0, testState.flagsRemaining)
    }
}