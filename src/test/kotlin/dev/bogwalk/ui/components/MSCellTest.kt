package dev.bogwalk.ui.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.ui.style.FLAG_DESCRIPTION
import dev.bogwalk.ui.style.MINE_DESCRIPTION
import dev.bogwalk.ui.util.GameState
import org.junit.Rule
import kotlin.test.Test

internal class MSCellTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `unclicked MSCell during and out of gameplay`() {
        val state = mutableStateOf(GameState.PLAYING)

        composeTestRule.setContent {
            MSCell(state.value, Cell(0 to 0), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = GameState.WON
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun `empty MSCell before and after clicking`() {
        val state = mutableStateOf(CellState.UNSELECTED)
        composeTestRule.setContent {
            MSCell(GameState.PLAYING, Cell(0 to 0, state = state.value), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = CellState.SELECTED
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun `MSCell with nearby mines before and after clicking`() {
        val state = mutableStateOf(CellState.UNSELECTED)
        composeTestRule.setContent {
            MSCell(GameState.PLAYING,
                Cell(0 to 0, neighbourMines = 3, state = state.value), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = CellState.SELECTED
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
            .assertTextEquals("3")
    }

    @Test
    fun `MSCell before and after flagging`() {
        val state = mutableStateOf(CellState.UNSELECTED)
        composeTestRule.setContent {
            MSCell(GameState.PLAYING,
                Cell(0 to 0, state = state.value), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = CellState.FLAGGED
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()
        composeTestRule
            .onNodeWithContentDescription(FLAG_DESCRIPTION)
            .assertExists()
    }

    @Test
    fun `MSCell with hidden mine before and after clicking`() {
        val state = mutableStateOf(CellState.UNSELECTED)
        composeTestRule.setContent {
            MSCell(GameState.PLAYING,
                Cell(0 to 0, neighbourMines = -1, state = state.value), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = CellState.SELECTED
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
        composeTestRule
            .onNodeWithContentDescription(MINE_DESCRIPTION)
            .assertExists()
    }

    @Test
    fun `MSCell with hidden mine after game lost`() {
        val state = mutableStateOf(GameState.PLAYING)
        composeTestRule.setContent {
            MSCell(state.value,
                Cell(0 to 0, neighbourMines = -1), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = GameState.LOST
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
        composeTestRule
            .onNodeWithContentDescription(MINE_DESCRIPTION)
            .assertExists()
    }

    @Test
    fun `MSCell with hidden mine after game won`() {
        val state = mutableStateOf(GameState.PLAYING)
        composeTestRule.setContent {
            MSCell(state.value,
                Cell(0 to 0, neighbourMines = -1), {}, {})
        }

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsEnabled()

        state.value = GameState.WON
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("(0,0)")
            .assertExists()
            .assertIsNotEnabled()
        composeTestRule
            .onNodeWithContentDescription(FLAG_DESCRIPTION)
            .assertExists()
    }
}