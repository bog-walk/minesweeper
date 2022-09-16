package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.GameLevel

@Composable
fun NewGameDialog(
    selectedLevel: GameLevel?,
    onCloseRequest: () -> Unit,
    onConfirm: (Pair<GameLevel?, List<Int>>) -> Unit
) {
    Dialog(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogWidth, dialogHeight)),
        title = GAME_MENU,
        resizable = false
    ) {
        GameOptions(selectedLevel, onConfirm)
    }
}

/**
 * A mock-table layout.
 */
@Composable
private fun GameOptions(
    selectedLevel: GameLevel?,
    onNewGame: (Pair<GameLevel?, List<Int>>) -> Unit
) {
    var selected by remember { mutableStateOf(selectedLevel) }
    var rows by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf("") }
    var mines by remember { mutableStateOf("") }
    println("$rows $columns $mines")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.align(Alignment.End).padding(vertical = smallPadding, horizontal = smallPadding * 2),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (header in listOf(HEADER1, HEADER2, HEADER3)) {
                Text(
                    text = header.padEnd(header.length + 1),
                    style = MaterialTheme.typography.body1
                )
            }
        }
        for (level in GameLevel.values()) {
            OptionsRow(level, selected == level) { selected = it }
        }
        // Custom row with 3x text input
        Row(
            modifier = Modifier.padding(smallPadding),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .selectable(
                        selected = selected == null,
                        role = Role.RadioButton,
                        onClick = { selected = null }
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == null,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = NumberColors.colors[1],
                        unselectedColor = MinesweeperColors.onPrimary
                    )
                )
                Spacer(Modifier.width(tinyPadding * 2))
                Text(
                    text = LEVEL_CUSTOM.padEnd(13),
                    style = MaterialTheme.typography.body1
                )
            }
            TextField(
                value = rows,
                onValueChange = { rows = it },
                modifier = Modifier.size(width = 50.dp, height = 50.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = rows.isValid()
            )
            Spacer(Modifier.width(smallPadding))
            TextField(
                value = columns,
                onValueChange = { columns = it },
                modifier = Modifier.size(width = 50.dp, height = 50.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = columns.isValid()
            )
            Spacer(Modifier.width(smallPadding))
            TextField(
                value = mines,
                onValueChange = { mines = it },
                modifier = Modifier.size(width = 60.dp, height = 50.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = mines.isValid(rows, columns)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    onNewGame(
                        selected to (selected?.values ?: listOf(rows.toInt(), columns.toInt(), mines.toInt()))
                    )
                },
                modifier = Modifier.padding(smallPadding),
                enabled = selected != null || (rows.isValid() && columns.isValid() && mines.isValid(rows, columns)),
                border = BorderStroke(2.dp, NumberColors.colors[1]),
                colors = ButtonDefaults.outlinedButtonColors(
                    disabledContentColor = MaterialTheme.colors.error
                )
            ) {
                Text(text = START_GAME, style = MaterialTheme.typography.button)
            }
            if (selected == null) {
                if (!rows.isValid()) {
                    Text(
                        text = "Height $ERROR_TEXT",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                } else if (!columns.isValid()) {
                    Text(
                        text = "Width $ERROR_TEXT",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                } else if (!mines.isValid(rows, columns)) {
                    Text(
                        text = "$ERROR_MINES_TEXT (${rows.toInt() * columns.toInt() / 5})",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}

private fun String.isValid(r: String = "", c: String = ""): Boolean {
    return isNotEmpty() && if (r.isEmpty()) {
        toInt() in 9..50
    } else {
        toInt() in 5..(r.toInt() * c.toInt() / 5)
    }
}

@Composable
private fun OptionsRow(
    level: GameLevel,
    isSelected: Boolean,
    onLevelSelect: (GameLevel) -> Unit
) {
    Row(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = { onLevelSelect(level) }
            )
            .padding(smallPadding),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = NumberColors.colors[1],
                unselectedColor = MinesweeperColors.onPrimary
            )
        )
        Spacer(Modifier.width(tinyPadding * 2))
        Text(
            text = level.name.padEnd(15),
            style = MaterialTheme.typography.body1
        )
        for ((i, number) in level.values.withIndex()) {
            Text(
                text = number.toString().padEnd(if (i == 0) 7 else 6),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Preview
@Composable
private fun GameOptionsPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogWidth, height = dialogHeight)
            .border(1.dp, Color.Red)) {
            GameOptions(GameLevel.INTERMEDIATE) {}
        }
    }
}

@Preview
@Composable
private fun GameOptionsInvalidPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogWidth, height = dialogHeight)
            .border(1.dp, Color.Red)) {
            GameOptions(null) {}
        }
    }
}