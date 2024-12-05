package dev.bogwalk.ui.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dev.bogwalk.ui.style.DIGITAL_TEST_TAG
import org.junit.Rule
import kotlin.test.Test

internal class DigitalScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `DigitalScreen splits a count into 3 digits`() {
        val count = mutableStateOf(0)
        composeTestRule.setContent {
            DigitalScreen(count.value)
        }

        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren()
            .assertAll(hasTestTag("Digit ZERO"))

        count.value++
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren()
            .filter(hasTestTag("Digit ZERO")).assertCountEquals(2)
        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren()
            .filter(hasTestTag("Digit ONE")).assertCountEquals(1)

        count.value = 456
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren().assertAny(hasTestTag("Digit FOUR"))
        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren().assertAny(hasTestTag("Digit FIVE"))
        composeTestRule.onNodeWithTag(DIGITAL_TEST_TAG)
            .onChildren().assertAny(hasTestTag("Digit SIX"))
    }
}