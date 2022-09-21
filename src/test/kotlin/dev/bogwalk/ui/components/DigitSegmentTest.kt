package dev.bogwalk.ui.components

import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class DigitSegmentTest {
    private val allExpected = listOf(
        listOf(0, 1, 3, 4, 5, 6), listOf(1, 4), listOf(0, 1, 2, 5, 6),
        listOf(0, 1, 2, 4, 5), listOf(1, 2, 3, 4), listOf(0, 2, 3, 4, 5), listOf(0, 2, 3, 4, 5, 6),
        listOf(0, 1, 4), listOf(0, 1, 2, 3, 4, 5, 6), listOf(0, 1, 2, 3, 4, 5)
    )

    @Test
    fun `isActive() returns correct segment mapping`() {
        for (n in 0..9) {
            val enum = DigitSegment.fromDigit(n)
            val actual = (0..6).filter { enum.isActive(it) }
            assertContentEquals(allExpected[n], actual)
        }
    }
}