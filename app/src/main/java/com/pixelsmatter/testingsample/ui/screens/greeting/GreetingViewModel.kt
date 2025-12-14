package com.pixelsmatter.testingsample.ui.screens.greeting

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("MutableCollectionMutableElement")
class GreetingViewModel @Inject constructor(
    @get:VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val processor: GreetingProcessor
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

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            processor.processAction(_uiState.value, GreetingScreenAction.ClearData).collect {
                // no-op, just consume the flow
            }
        }
    }
}

