package dev.bogwalk.ui.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.time_out
import dev.bogwalk.ui.style.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimeExceededDialog(
    onCloseRequest: () -> Unit
) {
    DialogWindow(
        onCloseRequest = { onCloseRequest() },
        state = rememberDialogState(size = DpSize(dialogSize / 2, dialogSize / 2)),
        title = "",
        resizable = false,
        content = { OutOfTimeError(onCloseRequest) }
    )
}

@Composable
private fun OutOfTimeError(
    onCloseRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.time_out),
            modifier = Modifier.padding(smallPadding),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )
        OutlinedButton(
            onClick = { onCloseRequest() },
            modifier = Modifier.padding(smallPadding),
            border = BorderStroke(tinyPadding, NumberColors.colors[1])
        ) {
            Text(text = START_GAME, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview
@Composable
private fun RulesDialogPreview() {
    MinesweeperTheme {
        Box(Modifier
            .size(width = dialogSize / 2, height = dialogSize / 2)
            .border(tinyPadding, Color.Red)
        ) {
            OutOfTimeError {}
        }
    }
}