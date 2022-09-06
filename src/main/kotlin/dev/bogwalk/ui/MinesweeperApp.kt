package dev.bogwalk.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.style.WINDOW_TITLE

@Composable
fun MinesweeperApp() {
    Text(WINDOW_TITLE)
}

@Preview
@Composable
private fun MinesweeperAppPreview() {
    MinesweeperTheme {
        MinesweeperApp()
    }
}