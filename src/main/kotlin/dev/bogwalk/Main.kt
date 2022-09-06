package dev.bogwalk

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.bogwalk.ui.MinesweeperApp
import dev.bogwalk.ui.style.MINESWEEPER_ICON
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.style.WINDOW_TITLE

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = WINDOW_TITLE,
        icon = painterResource(MINESWEEPER_ICON),
        resizable = false
    ) {
        MinesweeperTheme {
            MinesweeperApp()
        }
    }
}