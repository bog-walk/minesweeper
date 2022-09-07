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
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import dev.bogwalk.model.Cell
import dev.bogwalk.model.CellState
import dev.bogwalk.model.GameState
import dev.bogwalk.ui.drawEdge
import dev.bogwalk.ui.style.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MSCell(
    cell: Cell,
    gameState: GameState,
    onSelectCell: (Cell) -> Unit,
    onFlagCell: (Cell) -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("(${cell.coordinates.first},${cell.coordinates.second})")
            .requiredSize(cellSize)
            .mouseClickable(
                enabled = gameState == GameState.PLAYING && cell.state != CellState.SELECTED,
                onClickLabel = null,
                role = Role.Button,
                onClick = {
                    when {
                        buttons.isPrimaryPressed -> onSelectCell(cell)
                        buttons.isSecondaryPressed -> onFlagCell(cell)
                    }
                }
            )
            .background(
                color = if (gameState == GameState.LOST && cell.isMine && cell.state == CellState.SELECTED)
                    Color.Red else Color.LightGray
            )
            .drawWithCache {
              onDrawWithContent {
                  drawContent()
                  if (cell.state == CellState.SELECTED || gameState == GameState.LOST && cell.isMine) {
                      drawLine(Color.Gray, Offset.Zero, Offset(size.width, 0F))
                      drawLine(Color.Gray, Offset.Zero, Offset(0F, size.height))
                      drawLine(Color.Gray, Offset(size.width, 0F), Offset(size.width, size.height))
                      drawLine(Color.Gray, Offset(0F, size.height), Offset(size.width, size.height))
                  } else {
                      drawEdge(ELEVATION)
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
                        modifier = Modifier.matchParentSize()
                    )
                }
                if (gameState == GameState.WON && cell.isMine) {
                    Icon(
                        painter = painterResource(FLAG_ICON),
                        contentDescription = FLAG_DESCRIPTION,
                        modifier = Modifier.matchParentSize(),
                        tint = Color.Red
                    )
                }
            }
            CellState.FLAGGED -> {
                Icon(
                    painter = painterResource(FLAG_ICON),
                    contentDescription = FLAG_DESCRIPTION,
                    modifier = Modifier.matchParentSize(),
                    tint = Color.Red
                )
                if (gameState == GameState.LOST && !cell.isMine) {
                    Icon(
                        painter = painterResource(MINE_ICON),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.matchParentSize(),
                        tint = Color.Black
                    )
                }
            }
            CellState.SELECTED -> {
                when (cell.neighbourMines) {
                    -1 -> Icon(
                        painter = painterResource(MINE_ICON),
                        contentDescription = MINE_DESCRIPTION,
                        modifier = Modifier.matchParentSize(),
                        tint = Color.Black
                    )
                    0 -> {}
                    else -> Text(
                        text = cell.neighbourMines.toString(),
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        color = NumberColors.colors[cell.neighbourMines % 3]
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
            MSCell(Cell(0 to 0), GameState.PLAYING, {}, {})
            // disabled empty never clicked
            MSCell(Cell(0 to 0), GameState.WON, {}, {})
            // empty clicked
            MSCell(Cell(0 to 0, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // non-empty clicked
            MSCell(Cell(0 to 0, neighbourMines = 1, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // non-empty clicked
            MSCell(Cell(0 to 0, neighbourMines = 2, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // non-empty clicked
            MSCell(Cell(0 to 0, neighbourMines = 3, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // non-empty clicked
            MSCell(Cell(0 to 0, neighbourMines = 4, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // non-empty clicked
            MSCell(Cell(0 to 0, neighbourMines = 5, state = CellState.SELECTED), GameState.PLAYING, {}, {})
            // flagged
            MSCell(Cell(0 to 0, state = CellState.FLAGGED), GameState.PLAYING, {}, {})
            // clicked mine
            MSCell(Cell(0 to 0, isMine = true, neighbourMines = -1, state = CellState.SELECTED), GameState.LOST, {}, {})
            // disabled mine not clicked game lost
            MSCell(Cell(0 to 0, isMine = true, neighbourMines = -1, state = CellState.UNSELECTED), GameState.LOST, {}, {})
            // disabled mine not clicked / flagged game won
            MSCell(Cell(0 to 0, isMine = true, neighbourMines = -1, state = CellState.UNSELECTED), GameState.WON, {}, {})
            // flagged but not a mine when game lost
            MSCell(Cell(0 to 0, state = CellState.FLAGGED), GameState.LOST, {}, {})
        }
    }
}