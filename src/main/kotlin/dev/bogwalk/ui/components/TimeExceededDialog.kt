package dev.bogwalk.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.ui.style.*

@Composable
fun TimeExceededDialog(
    onCloseRequest: () -> Unit
) {
    Dialog(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogWidth / 2, dialogHeight / 2)),
        title = "",
        resizable = false
    ) {
        OutOfTimeError(onCloseRequest)
    }
}

@Composable
private fun OutOfTimeError(onCloseRequest: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = TIME_OUT,
            modifier = Modifier.padding(smallPadding),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
        OutlinedButton(
            onClick = { onCloseRequest() },
            modifier = Modifier.padding(smallPadding),
            border = BorderStroke(2.dp, NumberColors.colors[1])
        ) {
            Text(text = START_GAME, style = MaterialTheme.typography.button)
        }
    }
}

@Preview
@Composable
private fun RulesDialogPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogWidth / 2, height = dialogHeight / 2)
            .border(1.dp, Color.Red)) {
            OutOfTimeError {}
        }
    }
}