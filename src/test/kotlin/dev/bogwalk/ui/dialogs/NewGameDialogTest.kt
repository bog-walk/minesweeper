package dev.bogwalk.ui.dialogs

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.Level
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NewGameDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `NewGameDialog opens with current Level selected`() {
        val level = Level.INTERMEDIATE
        composeTestRule.setContent {
            GameOptions(level) {}
        }

        composeTestRule.onNodeWithTag(level.name)
            .assertIsSelected()
        composeTestRule.onNodeWithText(START_GAME)
            .assertIsEnabled()
    }

    @Test
    fun `NewGameDialog switches between levels correctly`() {
        var level: Level? = Level.INTERMEDIATE
        composeTestRule.setContent {
            GameOptions(level) { level = it.first }
        }

        composeTestRule.onNodeWithTag(level!!.name)
            .assertIsSelected()

        composeTestRule.onNodeWithTag(Level.BEGINNER.name)
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(Level.BEGINNER.name)
            .assertIsSelected()
        composeTestRule.onNodeWithText(START_GAME)
            .assertIsEnabled()
            .performClick()
        composeTestRule.waitForIdle()

        assertEquals(Level.BEGINNER, level)
    }

    @Test
    fun `custom row fields disabled until row selected`() {
        composeTestRule.setContent {
            GameOptions(Level.INTERMEDIATE) {}
        }

        composeTestRule.onNodeWithTag(LEVEL_CUSTOM)
            .assertIsNotSelected()
        for (ch in 'A'..'C') {
            composeTestRule.onNodeWithTag("$LEVEL_CUSTOM $ch")
                .assertIsNotEnabled()
        }

        composeTestRule.onNodeWithTag(LEVEL_CUSTOM)
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(LEVEL_CUSTOM)
            .assertIsSelected()
        for (ch in 'A'..'C') {
            composeTestRule.onNodeWithTag("$LEVEL_CUSTOM $ch")
                .assertIsEnabled()
        }
    }

    // Currently throwing NotImplementedError
    // due to performTextInput() in DesktopComposeTestRule.desktop.kt:198
    // https://github.com/JetBrains/compose-jb/issues/1177
    /*@Test
    fun `custom row only enables button when field inputs valid`() {
        composeTestRule.setContent {
            GameOptions(null) {}
        }

        composeTestRule.onNodeWithTag(LEVEL_CUSTOM)
            .assertIsSelected()
        composeTestRule.onNodeWithText(START_GAME)
            .assertIsNotEnabled()

        for (ch in 'A'..'C') {
            composeTestRule.onNodeWithTag("$LEVEL_CUSTOM $ch")
                .performTextInput("100")
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(START_GAME)
            .assertIsNotEnabled()

        for (ch in 'A'..'C') {
            composeTestRule.onNodeWithTag("$LEVEL_CUSTOM $ch")
                .performTextInput("10")
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(START_GAME)
            .assertIsEnabled()
    }*/

    @Test
    fun `correct error messages show when field inputs invalid`() {
        composeTestRule.setContent {
            GameOptions(null) {}
        }

        composeTestRule.onNodeWithTag(LEVEL_CUSTOM)
            .assertIsSelected()
        composeTestRule.onNodeWithText(HEIGHT_ERROR_TEXT)
            .assertExists("Height error not showing")
        // Currently throwing NotImplementedError
        // due to performTextInput() in DesktopComposeTestRule.desktop.kt:198
        // https://github.com/JetBrains/compose-jb/issues/1177
        /*
        composeTestRule.onNodeWithTag("$LEVEL_CUSTOM A")
            .performTextInput("20")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(WIDTH_ERROR_TEXT)
            .assertExists("Width error not showing")

        composeTestRule.onNodeWithTag("$LEVEL_CUSTOM B")
            .performTextInput("20")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("$MINES_ERROR_TEXT 80")
            .assertExists("Mines error not showing")

        composeTestRule.onNodeWithTag("$LEVEL_CUSTOM C")
            .performTextInput("20")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("$MINES_ERROR_TEXT 80")
            .assertDoesNotExist()
        */
    }
}