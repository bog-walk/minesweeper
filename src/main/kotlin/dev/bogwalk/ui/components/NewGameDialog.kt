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
    var rows by remember { mutableStateOf(selectedLevel?.values?.get(0) ?: 20) }
    var columns by remember { mutableStateOf(selectedLevel?.values?.get(1) ?: 30) }
    var mines by remember { mutableStateOf(selectedLevel?.values?.get(2) ?: 120) }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .padding(smallPadding),
            horizontalArrangement = Arrangement.End,
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
            OptionsRow(level, selected == level) {
                selected = it
                rows = it.values[0]
                columns = it.values[1]
                mines = it.values[2]
                println("${selected?.name ?: "Custom"} $rows $columns $mines")
            }
        }
        // Custom row with 3x text input
        Row(
            modifier = Modifier.align(Alignment.Start).padding(smallPadding),
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
                    text = LEVEL_CUSTOM.padEnd(15),
                    style = MaterialTheme.typography.body1
                )
            }
            TextField(
                value = rows.toString(),
                onValueChange = { rows = it.trim().toInt() },
                modifier = Modifier.size(width = 45.dp, height = 25.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = rows !in 5..40
            )
            Spacer(Modifier.width(smallPadding))
            TextField(
                value = columns.toString(),
                onValueChange = { columns = it.trim().toInt() },
                modifier = Modifier.size(width = 45.dp, height = 25.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = columns !in 5..80
            )
            Spacer(Modifier.width(smallPadding))
            TextField(
                value = mines.toString(),
                onValueChange = { mines = it.trim().toInt() },
                modifier = Modifier.size(width = 45.dp, height = 25.dp),
                enabled = selected == null,
                textStyle = MaterialTheme.typography.body1,
                isError = mines !in 3..rows * columns
            )
        }
        OutlinedButton(
            onClick = { onNewGame(selected to listOf(rows, columns, mines)) },
            modifier = Modifier.align(Alignment.Start).padding(smallPadding),
            border = BorderStroke(2.dp, NumberColors.colors[1])
        ) {
            Text(text = START_GAME, style = MaterialTheme.typography.button)
        }
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