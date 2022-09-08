package dev.bogwalk.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.model.Cell
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.components.MSGrid
import org.junit.Rule
import kotlin.test.Test

internal class MSGridTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `MSGrid initial load is correct`() {
        val rows = 9
        val columns = 9
        val board = Array(rows) { r -> Array(columns) { c -> Cell(r to c) } }
        composeTestRule.setContent {
            MSGrid(board, GameState.PLAYING, {}, {})
        }

        composeTestRule
            .onAllNodes(hasClickAction())
            .assertCountEquals(rows * columns)
            .assertAll(isEnabled())
    }
}