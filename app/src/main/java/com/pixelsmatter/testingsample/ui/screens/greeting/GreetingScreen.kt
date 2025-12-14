package com.pixelsmatter.testingsample.ui.screens.greeting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelsmatter.testingsample.ui.theme.AppTheme

@Composable
fun GreetingScreen(
    modifier: Modifier = Modifier,
    viewModel: GreetingViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    GreetingContent(
        uiState = uiState.value,
        modifier = modifier,
        onAction = viewModel::onAction
    )
}

@Composable
private fun GreetingContent(
    uiState: GreetingUiState,
    modifier: Modifier = Modifier,
    onAction: (GreetingScreenAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.displayValue)
        }
        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = {
                onAction.invoke(GreetingScreenAction.RetrieveData)
            }) {
            Text(text = "Retrieve Data")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingScreenPreview() {
    AppTheme {
        GreetingContent(
            uiState = GreetingUiState(
                displayValue = "Hello"
            ),
            onAction = {}
        )
    }
}
