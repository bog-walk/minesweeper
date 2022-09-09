package dev.bogwalk.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

object Timer {
    var seconds by mutableStateOf(0)
    var outOfTime by mutableStateOf(false)

    private var coroutineScope = CoroutineScope(Dispatchers.Default)
    private const val LIMIT = 1000

    fun start() {
        coroutineScope.launch {
            while (seconds < LIMIT) {
                delay(1000L)
                seconds++
            }
            outOfTime = true
        }
    }

    fun end() {
        coroutineScope.cancel()
    }

    fun reset() {
        seconds = 0
        outOfTime = false
        coroutineScope = CoroutineScope(Dispatchers.Default)
    }
}