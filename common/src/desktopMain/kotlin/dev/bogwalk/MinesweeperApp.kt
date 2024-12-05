import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.model.GameGrid
import dev.bogwalk.ui.MinesweeperApp
import dev.bogwalk.ui.dialogs.NewGameDialog
import dev.bogwalk.ui.dialogs.RulesDialog
import dev.bogwalk.ui.dialogs.TimeExceededDialog
import dev.bogwalk.ui.style.MinesweeperTheme
import dev.bogwalk.ui.util.Level
import dev.bogwalk.ui.util.MinesweeperAppState
import dev.bogwalk.ui.util.calcWindowHeight
import dev.bogwalk.ui.util.calcWindowWidth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationScope.MinesweeperAppDesktop() {
    val windowState = rememberWindowState(
        width = Level.INTERMEDIATE.windowWidth(), height = Level.INTERMEDIATE.windowHeight()
    )
    var gameState by remember { mutableStateOf(MinesweeperAppState()) }

    var isNGDialogOpen by remember { mutableStateOf(false) }
    var isRulesDialogOpen by remember { mutableStateOf(false) }

    var currentLevel: Level? = Level.INTERMEDIATE

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = stringResource(Res.string.window_title),
        icon = painterResource(Res.drawable.mine),
        resizable = false
    ) {
        MenuBar {
            Menu(text = stringResource(Res.string.options_menu)) {
                Item(text = stringResource(Res.string.games_menu), mnemonic = 'N') {
                    gameState.timer.pause()
                    isNGDialogOpen = true
                }
                Item(text = stringResource(Res.string.rules_menu), mnemonic = 'R') {
                    gameState.timer.pause()
                    isRulesDialogOpen = true
                }
            }
        }

        MinesweeperTheme {
            if (isNGDialogOpen) {
                NewGameDialog(currentLevel, { isNGDialogOpen = false; gameState.timer.restart() }) {
                    currentLevel = it.first
                    gameState.timer.end()
                    gameState = MinesweeperAppState(GameGrid(it.second[0], it.second[1], it.second[2]))
                    windowState.size = windowState.size.copy(
                        currentLevel?.windowWidth() ?: calcWindowWidth(it.second[1]),
                        currentLevel?.windowHeight() ?: calcWindowHeight(it.second[0])
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