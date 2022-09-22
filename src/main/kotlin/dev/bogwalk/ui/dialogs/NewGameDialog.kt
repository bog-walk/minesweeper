package dev.bogwalk.ui.dialogs

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.ui.style.*
import dev.bogwalk.ui.util.Level

@Composable
fun NewGameDialog(
    selectedLevel: Level?,
    onCloseRequest: () -> Unit,
    onNewGame: (Pair<Level?, List<Int>>) -> Unit
) {
    Dialog(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogSize, dialogSize)),
        title = GAME_MENU,
        resizable = false
    ) {
        GameOptions(selectedLevel, onNewGame)
    }
}

@Composable
internal fun GameOptions(
    selectedLevel: Level?,
    onNewGame: (Pair<Level?, List<Int>>) -> Unit
) {
    var selected by remember { mutableStateOf(selectedLevel) }
    var rows by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf("") }
    var mines by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = smallPadding, horizontal = cellSize),
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

        for (level in Level.values()) {
            OptionsRow(level, selected == level) { selected = it }
        }

        // custom row with 3x text input
        Row(
            modifier = Modifier
                .testTag(LEVEL_CUSTOM)
                .selectable(
                    selected = selected == null,
                    role = Role.RadioButton,
                    onClick = { selected = null }
                )
                .padding(smallPadding),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val inputSize = 50.dp

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
            OutlinedTextField(
                value = rows,
                onValueChange = { rows = it },
                modifier = Modifier.testTag("$LEVEL_CUSTOM A").size(inputSize),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = !rows.isValid(isHeight = true),
                singleLine = true
            )
            Spacer(Modifier.width(smallPadding))
            OutlinedTextField(
                value = columns,
                onValueChange = { columns = it },
                modifier = Modifier.testTag("$LEVEL_CUSTOM B").size(inputSize),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = !columns.isValid(isHeight = false),
                singleLine = true
            )
            Spacer(Modifier.width(smallPadding))
            OutlinedTextField(
                value = mines,
                onValueChange = { mines = it },
                modifier = Modifier.testTag("$LEVEL_CUSTOM C").size(inputSize + smallPadding, inputSize),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = rows.isEmpty() || columns.isEmpty() || !mines.isValid(rows, columns),
                singleLine = true
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
                enabled = selected != null || (rows.isValid(true) && columns.isValid(false) && mines.isValid(rows, columns)),
                border = BorderStroke(tinyPadding, NumberColors.colors[1])
            ) {
                Text(text = START_GAME, style = MaterialTheme.typography.button)
            }
            if (selected == null) {
                if (!rows.isValid(isHeight = true)) {
                    Text(
                        text = HEIGHT_ERROR_TEXT,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                } else if (!columns.isValid(isHeight = false)) {
                    Text(
                        text = WIDTH_ERROR_TEXT,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                } else if (!mines.isValid(rows, columns)) {
                    Text(
                        text = "$MINES_ERROR_TEXT ${getMaxMines(rows, columns)}",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}

private fun String.isValid(isHeight: Boolean) = isNotEmpty() && toInt() in 9..(if (isHeight) 30 else 50)

private fun String.isValid(r: String, c: String) = isNotEmpty() && toInt() in 5..getMaxMines(r, c)

// amount of mines should not exceed 20% of available cell count
// short circuit evaluation means this will not be called with empty strings
private fun getMaxMines(r: String, c: String): Int = r.toInt() * c.toInt() / 5

@Composable
private fun OptionsRow(
    level: Level,
    isSelected: Boolean,
    onLevelSelect: (Level) -> Unit
) {
    Row(
        modifier = Modifier
            .testTag(level.name)
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
            .size(width = dialogSize, height = dialogSize)
            .border(tinyPadding, Color.Red)) {
            GameOptions(Level.INTERMEDIATE) {}
        }
    }
}

@Preview
@Composable
private fun GameOptionsInvalidPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogSize, height = dialogSize)
            .border(tinyPadding, Color.Red)) {
            GameOptions(null) {}
        }
    }
}