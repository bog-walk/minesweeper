package dev.bogwalk.ui.integration

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import dev.bogwalk.ui.style.DIGITAL_TEST_TAG
import dev.bogwalk.ui.style.GRID_TEST_TAG
import dev.bogwalk.ui.style.RESET_DEFAULT_DESCRIPTION

fun ComposeContentTestRule.assertInitialLoad(
    expectedCells: Int = 25
) {
    // reset button
    onNodeWithContentDescription(RESET_DEFAULT_DESCRIPTION)
        .assertExists()
    // flags remaining screen
    onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
        .onChildren()
        .filter(hasTestTag("Digit ZERO")).assertCountEquals(2)
    onAllNodesWithTag(DIGITAL_TEST_TAG)[0]
        .onChildren()
        .filter(hasTestTag("Digit THREE")).assertCountEquals(1)
    // seconds passed screen
    onAllNodesWithTag(DIGITAL_TEST_TAG)[1]
        .onChildren()
        .assertAll(hasTestTag("Digit ZERO"))
    // main game grid
    onNodeWithTag(GRID_TEST_TAG)
        .onChildren()
        .assertCountEquals(expectedCells).assertAll(isEnabled())
}