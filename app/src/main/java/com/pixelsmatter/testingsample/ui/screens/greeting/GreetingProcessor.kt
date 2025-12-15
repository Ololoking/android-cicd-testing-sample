package com.pixelsmatter.testingsample.ui.screens.greeting

import com.pixelsmatter.domain.VeryImportantRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GreetingProcessor @Inject constructor(
    private val importantRepository: VeryImportantRepository
) {
    fun processAction(
        currentState: GreetingUiState,
        action: GreetingScreenAction
    ): Flow<GreetingUiState> = flow {
        when (action) {
            GreetingScreenAction.RetrieveData -> {
                emit(currentState.copy(displayValue = "Loading...", isLoading = true, error = null))
                try {
                    delay(500) // Simulate network delay
                    val data = importantRepository.retrieveData()
                    emit(
                        currentState.copy(
                            displayValue = data.toString(),
                            isLoading = false,
                            error = null
                        )
                    )
                } catch (e: Exception) {
                    emit(currentState.copy(isLoading = false, error = e.message ?: "Unknown error"))
                }
            }

            GreetingScreenAction.ClearData ->
                importantRepository.clearData()
        }
    }
}

