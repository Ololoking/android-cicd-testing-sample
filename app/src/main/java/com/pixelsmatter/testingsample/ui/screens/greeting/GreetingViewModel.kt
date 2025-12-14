package com.pixelsmatter.testingsample.ui.screens.greeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val processor: GreetingProcessor
) : ViewModel() {

    private val _uiState = MutableStateFlow(GreetingUiState())
    val uiState: StateFlow<GreetingUiState> = _uiState.asStateFlow()

    fun onAction(action: GreetingScreenAction) {
        viewModelScope.launch {
            processor.processAction(_uiState.value, action).collect { newState ->
                _uiState.value = newState
            }
        }
    }
}