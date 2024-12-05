package dev.bogwalk.ui.integration

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.model.TestGrid
import dev.bogwalk.ui.MinesweeperApp
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.MinesweeperAppState
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
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
        composeTestRule.assertInitialLoad(81)

        // select first cell to clear all but mines for instant win :)
        composeTestRule.onNodeWithTag("(0,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_WON_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .assertAll(isNotEnabled())
            .filter(hasTextExactly("1")).assertCountEquals(11)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(hasTextExactly("2")).assertCountEquals(4)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(3)
        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .assertAll(hasTestTag("Digit ZERO"))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `MinesweeperApp() correct for lost game`() {
        val testGrid = TestGrid(listOf(0 to 0, 2 to 1, 4 to 0))
        val testState = MinesweeperAppState(testGrid)
        composeTestRule.setContent {
            MinesweeperApp(testState)
        }

        // initial set up as expected
        composeTestRule.assertInitialLoad()

        // select first cell
        composeTestRule.onNodeWithTag("(4,4)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(isNotEnabled()).assertCountEquals(19)
            .filter(hasTextExactly("1")).assertCountEquals(5)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(hasTextExactly("2")).assertCountEquals(2)

        // correctly flag a mine
        composeTestRule.onNodeWithTag("(2,1)").performMouseInput { rightClick() }
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit ZERO")).assertCountEquals(2)
        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit TWO")).assertCountEquals(1)
        composeTestRule.onNodeWithTag("(2,1)")
            .assert(hasContentDescription(FLAG_DESCRIPTION))

        // select a mine :(
        composeTestRule.onNodeWithTag("(0,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_LOST_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .assertAll(isNotEnabled())
            .filter(hasContentDescription(MINE_DESCRIPTION)).assertCountEquals(2)
        // correctly flagged mines stay as flags even if game lost
        composeTestRule.onNodeWithTag("(2,1)")
            .assert(hasContentDescription(FLAG_DESCRIPTION))
        // unselected cells should stay unselected once game over
        for (x in 1..3) {
            composeTestRule.onNodeWithTag("($x,0)", useUnmergedTree = true)
                .onChildren().assertCountEquals(0)
        }

        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit ZERO")).assertCountEquals(2)
        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit TWO")).assertCountEquals(1)
    }

    @Test
    fun `MinesweeperApp() correct for mid-game reset and win game`() {
        val testGrid = TestGrid(listOf(0 to 0, 2 to 1, 4 to 0))
        val testState = MinesweeperAppState(testGrid)
        composeTestRule.setContent {
            MinesweeperApp(testState)
        }

        // initial set up as expected
        composeTestRule.assertInitialLoad()

        // select a cell
        composeTestRule.onNodeWithTag("(4,4)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(isNotEnabled()).assertCountEquals(19)
            .filter(hasTextExactly("1")).assertCountEquals(5)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(hasTextExactly("2")).assertCountEquals(2)

        // reset board
        composeTestRule.onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.assertInitialLoad()

        // select first cell
        composeTestRule.onNodeWithTag("(2,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("(2,0)")
            .assertIsNotEnabled().assertTextEquals("1")
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(isEnabled()).assertCountEquals(24)

        // flag 2 mines
        composeTestRule.onNodeWithTag("(2,1)").performMouseInput { rightClick() }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("(0,0)").performMouseInput { rightClick() }
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit ZERO")).assertCountEquals(2)
        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .filter(hasTestTag("Digit ONE")).assertCountEquals(1)
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(2)

        // select second cell to clear most of grid
        composeTestRule.onNodeWithTag("(0,2)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .filter(isNotEnabled()).assertCountEquals(20)
            .filter(hasTextExactly("1")).assertCountEquals(6)

        // select last 2 non-mine cells to win game :)
        composeTestRule.onNodeWithTag("(1,0)").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("(3,0)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(RESET_WON_DESCRIPTION).assertExists()
        composeTestRule.onNodeWithTag(GRID_TEST_TAG)
            .onChildren()
            .assertAll(isNotEnabled())
            .filter(hasContentDescription(FLAG_DESCRIPTION)).assertCountEquals(3)
        composeTestRule.onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
            .onChildren()
            .assertAll(hasTestTag("Digit ZERO"))
    }
}