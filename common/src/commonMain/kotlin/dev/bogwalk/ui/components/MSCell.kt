package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.flag
import dev.bogwalk.common.generated.resources.mine
import dev.bogwalk.common.generated.resources.mine_x
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.GameState
import dev.bogwalk.ui.util.drawBevelEdge
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MSCell(
    gameState: GameState,
    cell: Cell,
    onSelectCell: (Pair<Int, Int>) -> Unit,
    onFlagCell: (Pair<Int, Int>) -> Unit
) {
    Box(
        modifier = Modifier
            .semantics {
                role = Role.Button

                if (gameState != GameState.PLAYING || cell.state == CellState.SELECTED) disabled()
            }
            .testTag("(${cell.coordinates.first},${cell.coordinates.second})")
            .requiredSize(cellSize)
            .background(color = getBackgroundColor(gameState, cell))
            .drawWithCache {
                onDrawBehind {
                    if (cell.state == CellState.SELECTED ||
                        gameState == GameState.LOST && cell.isMine && cell.state == CellState.UNSELECTED
                    ) {
                        drawFlatEdge()
                    } else {
                        drawBevelEdge(BEVEL_STROKE_SM)
                    }
                }
            }
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Primary),
                onClick = { onSelectCell(cell.coordinates) }
            )
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = { onFlagCell(cell.coordinates) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val cellPadding = 3.dp

        when (cell.state) {
            CellState.UNSELECTED -> {
                if (gameState == GameState.LOST && cell.isMine) {
                    Icon(
                        painter = painterResource(Res.drawable.mine),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.padding(cellPadding),
                        tint = Color.Unspecified
                    )
                }
                if (gameState == GameState.WON && cell.isMine) {
                    Icon(
                        painter = painterResource(Res.drawable.flag),
                        contentDescription = FLAG_DESCRIPTION,
                        modifier = Modifier.padding(cellPadding),
                        tint = Color.Unspecified
                    )
                }
                // no content should be drawn otherwise
            }
            CellState.SELECTED -> {
                when (cell.neighbourMines) {
                    -1 -> Icon(
                        painter = painterResource(Res.drawable.mine),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.padding(cellPadding),
                        tint = Color.Unspecified
                    )
                    0 -> {}
                    else -> Text(
                        text = cell.neighbourMines.toString(),
                        color = NumberColors.colors[cell.neighbourMines % 6],
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,  // removes the need for Modifier.wrapContentSize(Alignment.Center, true)
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            CellState.FLAGGED -> {
                if (gameState == GameState.LOST && !cell.isMine) {
                    Icon(
                        painter = painterResource(Res.drawable.mine_x),
                        contentDescription = MINE_X_DESCRIPTION,
                        modifier = Modifier.padding(cellPadding),
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.flag),
                        contentDescription = FLAG_DESCRIPTION,
                        modifier = Modifier.padding(cellPadding),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

private fun getBackgroundColor(gameState: GameState, cell: Cell): Color {
    return when (cell.state) {
        CellState.SELECTED -> {
            if (gameState == GameState.LOST && cell.isMine) {
                MinesweeperColors.onError
            } else {
                MinesweeperColors.primary
            }
        }
        CellState.FLAGGED -> MinesweeperColors.secondary
        CellState.UNSELECTED -> if (gameState == GameState.LOST && cell.isMine) {
            MinesweeperColors.primary
        } else {
            MinesweeperColors.secondary
        }
    }
}

private fun DrawScope.drawFlatEdge() {
    drawLine(MinesweeperColors.onPrimary, Offset.Zero, Offset(size.width, 0f))
    drawLine(MinesweeperColors.onPrimary, Offset.Zero, Offset(0f, size.height))
    drawLine(MinesweeperColors.onPrimary, Offset(size.width, 0f), Offset(size.width, size.height))
    drawLine(MinesweeperColors.onPrimary, Offset(0f, size.height), Offset(size.width, size.height))
}

@Preview
@Composable
private fun MSCellPreview() {
    MinesweeperTheme {
        Column(
            modifier = Modifier.padding(smallPadding).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // initial default enabled during gameplay
            MSCell(GameState.PLAYING, Cell(0 to 0), {}, {})
            // disabled empty (not a mine & no neighbours) unselected, game won
            MSCell(GameState.WON, Cell(0 to 0), {}, {})
            // disabled empty (not a mine & no neighbours) unselected, game lost
            MSCell(GameState.LOST, Cell(0 to 0), {}, {})
            // empty (not a mine & no neighbours) selected during gameplay
            MSCell(GameState.PLAYING, Cell(0 to 0, state = CellState.SELECTED), {}, {})
            // empty (not a mine & no neighbours) selected, after game lost
            MSCell(GameState.LOST, Cell(0 to 0, state = CellState.SELECTED), {}, {})
            // empty (not a mine & no neighbours) selected, after game won
            MSCell(GameState.WON, Cell(0 to 0, state = CellState.SELECTED), {}, {})
            // flagged during gameplay
            MSCell(GameState.PLAYING, Cell(0 to 0, state = CellState.FLAGGED), {}, {})
            // flagged (incorrectly) after game lost
            MSCell(GameState.LOST, Cell(0 to 0, state = CellState.FLAGGED), {}, {})
        }
    }
}

@Preview
@Composable
private fun MSCellWithMinePreview() {
    MinesweeperTheme {
        Column(
            modifier = Modifier.padding(smallPadding).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // unselected mine during gameplay
            MSCell(GameState.PLAYING, Cell(0 to 0, neighbourMines = -1, state = CellState.UNSELECTED), {}, {})
            // flagged mine during gameplay
            MSCell(GameState.PLAYING, Cell(0 to 0, neighbourMines = -1, state = CellState.FLAGGED), {}, {})
            // selected mine -> game lost
            MSCell(GameState.LOST, Cell(0 to 0, neighbourMines = -1, state = CellState.SELECTED), {}, {})
            // unselected mine after game lost
            MSCell(GameState.LOST, Cell(0 to 0, neighbourMines = -1, state = CellState.UNSELECTED), {}, {})
            // flagged mine after game lost
            MSCell(GameState.LOST, Cell(0 to 0, neighbourMines = -1, state = CellState.FLAGGED), {}, {})
            // unselected mine after game won
            MSCell(GameState.WON, Cell(0 to 0, neighbourMines = -1, state = CellState.UNSELECTED), {}, {})
            // flagged mine after game won
            MSCell(GameState.WON, Cell(0 to 0, neighbourMines = -1, state = CellState.FLAGGED), {}, {})
        }
    }
}

@Preview
@Composable
private fun MSCellWithTextPreview() {
    MinesweeperTheme {
        Column(
            modifier = Modifier.padding(smallPadding).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (n in 1..8) {
                MSCell(GameState.PLAYING, Cell(0 to 0, neighbourMines = n, state = CellState.SELECTED), {}, {})
            }
        }
    }
}