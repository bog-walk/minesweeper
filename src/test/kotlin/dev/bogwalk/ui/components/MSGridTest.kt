package dev.bogwalk.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.model.Cell
import dev.bogwalk.ui.style.GRID_TEST_TAG
import dev.bogwalk.ui.util.GameState
import org.junit.Rule
import kotlin.test.Test

internal class MSGridTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `MSGrid initial load is correct`() {
        val rows = 9
        val columns = 9
        val board = List(rows) { r -> List(columns) { c -> Cell(r to c) } }.flatten()
        composeTestRule.setContent {
            MSGrid(rows, columns, board, GameState.PLAYING, {}, {})
        }

        composeTestRule.onNodeWithTag(GRID_TEST_TAG).onChildren()
            .filter(hasClickAction())
            .assertCountEquals(rows * columns)
            .assertAll(isEnabled())
    }
}