package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.util.drawBevelEdge
import dev.bogwalk.ui.style.*

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
            .testTag("(${cell.coordinates.first},${cell.coordinates.second})")
            .requiredSize(cellSize)
            .mouseClickable(
                enabled = gameState == GameState.PLAYING && cell.state != CellState.SELECTED,
                onClickLabel = "(${cell.coordinates.first},${cell.coordinates.second})",
                role = Role.Button,
                onClick = {
                    when {
                        buttons.isPrimaryPressed -> onSelectCell(cell.coordinates)
                        buttons.isSecondaryPressed -> onFlagCell(cell.coordinates)
                    }
                }
            )
            .background(
                color = if (gameState == GameState.LOST && cell.isMine) {
                    when (cell.state) {
                        CellState.SELECTED -> MinesweeperColors.error
                        else -> MinesweeperColors.primary
                    }
                } else {
                    when (cell.state) {
                        CellState.SELECTED -> MinesweeperColors.primary
                        else -> MinesweeperColors.secondary
                    }
                }
            )
            .drawWithCache {
                onDrawBehind {
                    if (cell.state == CellState.SELECTED || (gameState == GameState.LOST && cell.isMine)) {
                        drawLine(MinesweeperColors.onPrimary, Offset.Zero, Offset(size.width, 0F))
                        drawLine(MinesweeperColors.onPrimary, Offset.Zero, Offset(0F, size.height))
                        drawLine(MinesweeperColors.onPrimary, Offset(size.width, 0F), Offset(size.width, size.height))
                        drawLine(MinesweeperColors.onPrimary, Offset(0F, size.height), Offset(size.width, size.height))
                    } else {
                        drawBevelEdge(BEVEL_STROKE)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (cell.state) {
            CellState.UNSELECTED -> {
                if (gameState == GameState.LOST && cell.isMine) {
                    Icon(
                        painter = painterResource(MINE_ICON),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.padding(tinyPadding).matchParentSize()
                    )
                }
                if (gameState == GameState.WON && cell.isMine) {
                    Icon(
                        painter = painterResource(FLAG_ICON),
                        contentDescription = FLAG_DESCRIPTION,
                        modifier = Modifier.padding(tinyPadding).matchParentSize(),
                        tint = Color.Red
                    )
                }
            }
            CellState.SELECTED -> {
                when (cell.neighbourMines) {
                    -1 -> Icon(
                        painter = painterResource(MINE_ICON),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.matchParentSize(),
                        tint = Color.White
                    )
                    0 -> {}
                    else -> Text(
                        text = cell.neighbourMines.toString(),
                        modifier = Modifier.wrapContentSize(Alignment.Center, true),
                        color = NumberColors.colors[cell.neighbourMines % 6],
                        textAlign = TextAlign.Center
                    )
                }
            }
            CellState.FLAGGED -> {
                if (gameState == GameState.LOST && !cell.isMine) {
                    Icon(
                        painter = painterResource(MINE_ICON),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.padding(tinyPadding).matchParentSize(),
                        tint = Color.White
                    )
                } else {
                    Icon(
                        painter = painterResource(FLAG_ICON),
                        contentDescription = FLAG_DESCRIPTION,
                        modifier = Modifier.padding(tinyPadding).matchParentSize(),
                        tint = Color.Red
                    )
                }
            }
        }
    }
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
            // initial default enabled
            MSCell(GameState.PLAYING, Cell(0 to 0), {}, {})
            // disabled empty never clicked
            MSCell(GameState.WON, Cell(0 to 0), {}, {})
            // empty clicked
            MSCell(GameState.PLAYING, Cell(0 to 0, state = CellState.SELECTED), {}, {})
            // flagged
            MSCell(GameState.PLAYING, Cell(0 to 0, state = CellState.FLAGGED), {}, {})
            // clicked mine
            MSCell(GameState.LOST, Cell(0 to 0, neighbourMines = -1, state = CellState.SELECTED), {}, {})
            // disabled mine not clicked game lost
            MSCell(GameState.LOST, Cell(0 to 0, neighbourMines = -1, state = CellState.UNSELECTED), {}, {})
            // disabled mine not clicked / flagged game won
            MSCell(GameState.WON, Cell(0 to 0, neighbourMines = -1, state = CellState.UNSELECTED), {}, {})
            // flagged but not a mine when game lost
            MSCell(GameState.LOST, Cell(0 to 0, state = CellState.FLAGGED), {}, {})
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