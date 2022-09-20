package dev.bogwalk.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

class Timer {
    var seconds by mutableStateOf(0)
    var outOfTime by mutableStateOf(false)

    private var isTiming = false
    private var coroutineScope = CoroutineScope(Dispatchers.Default)
    private val limit = 1000L

    fun start() {
        coroutineScope.launch {
            isTiming = true
            while (isTiming) {
                delay(limit)
                seconds++
                if (seconds >= limit - 1) {
                    outOfTime = true
                    break
                }
            }
        }
    }

    fun pause() {
        isTiming = false
    }

    fun restart() {
        // so that checking menu from unstarted game won't start Timer
        if (seconds > 0) start()
    }

    fun end() {
        coroutineScope.cancel()
        isTiming = false
    }

    fun reset() {
        coroutineScope.cancel()
        seconds = 0
        outOfTime = false
        coroutineScope = CoroutineScope(Dispatchers.Default)
        isTiming = false
    }
}