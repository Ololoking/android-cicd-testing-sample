package com.pixelsmatter.testingsample.ui.screens.greeting

data class GreetingUiState(
    val displayValue: String = "Hello",
    val isLoading: Boolean = false,
    val error: String? = null,
)