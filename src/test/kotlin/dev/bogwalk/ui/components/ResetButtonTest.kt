package dev.bogwalk.ui.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import dev.bogwalk.ui.style.RESET_DEFAULT_DESCRIPTION
import dev.bogwalk.ui.style.RESET_LOST_DESCRIPTION
import dev.bogwalk.ui.style.RESET_WON_DESCRIPTION
import dev.bogwalk.ui.util.GameState
import org.junit.Rule
import kotlin.test.Test

internal class ResetButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `ResetButton changes icon for each GameState but is always enabled`() {
        val state = mutableStateOf(GameState.PLAYING)
        composeTestRule.setContent {
            ResetButton(state.value) {}
        }

        composeTestRule
            .onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION)
            .assertExists()
            .assertIsEnabled()

        state.value = GameState.WON
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(RESET_WON_DESCRIPTION)
            .assertExists()
            .assertIsEnabled()

        state.value = GameState.LOST
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(RESET_LOST_DESCRIPTION)
            .assertExists()
            .assertIsEnabled()
    }
}