package dev.bogwalk

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bogwalk.model.GameGrid
import dev.bogwalk.ui.MinesweeperApp
import dev.bogwalk.ui.dialogs.NewGameDialog
import dev.bogwalk.ui.dialogs.RulesDialog
import dev.bogwalk.ui.dialogs.TimeExceededDialog
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.Level
import dev.bogwalk.ui.util.MinesweeperAppState

fun main() = application {
    var currentLevel: Level? = Level.INTERMEDIATE

    val windowState = rememberWindowState(width = Level.INTERMEDIATE.size.first, height = Level.INTERMEDIATE.size.second)
    var gameState by remember { mutableStateOf(MinesweeperAppState()) }

    var isNGDialogOpen by remember { mutableStateOf(false) }
    var isRulesDialogOpen by remember { mutableStateOf(false) }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = WINDOW_TITLE,
        icon = painterResource(MINE_ICON),
        resizable = false
    ) {
        MinesweeperTheme {
            MenuBar {
                Menu(text = OPTIONS_MENU, mnemonic = 'O') {
                    Item(text = GAME_MENU, mnemonic = 'N') {
                        gameState.timer.pause()
                        isNGDialogOpen = true
                    }
                    Item(text = RULES_MENU, mnemonic = 'R') {
                        gameState.timer.pause()
                        isRulesDialogOpen = true
                    }
                }
            }
            if (isNGDialogOpen) {
                NewGameDialog(currentLevel, { isNGDialogOpen = false; gameState.timer.restart() }) {
                    currentLevel = it.first
                    gameState.timer.end()
                    gameState = MinesweeperAppState(GameGrid(it.second[0], it.second[1], it.second[2]))
                    windowState.size = windowState.size.copy(
                        currentLevel?.size?.first ?: calcWindowWidth(it.second[1]),
                        currentLevel?.size?.second ?: calcWindowHeight(it.second[0])
                    )
                    isNGDialogOpen = false
                }
            }
            if (isRulesDialogOpen) {
                RulesDialog {
                    isRulesDialogOpen = false
                    gameState.timer.restart()
                }
            }
            if (gameState.timer.outOfTime) {
                TimeExceededDialog { gameState.resetBoard() }
            }
            MinesweeperApp(gameState)
        }
    }
}

private fun calcWindowWidth(columns: Int) = (columns * cellSize.value).dp + staticWidth

private fun calcWindowHeight(rows: Int) = (rows * cellSize.value).dp + staticHeight