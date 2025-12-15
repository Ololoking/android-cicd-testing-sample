package com.pixelsmatter.testingsample.ui.screens.greeting

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GreetingViewModelTest {

    private lateinit var mockProcessor: GreetingProcessor
    private lateinit var viewModel: GreetingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockProcessor = mockk(relaxed = true)
        viewModel = GreetingViewModel(mockProcessor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have default values`() {
        // Assert
        val state = viewModel.uiState.value
        assertEquals("Hello", state.displayValue)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `onAction should call processor and update state to loading`() = runTest {
        // Arrange
        val action = GreetingScreenAction.RetrieveData
        val loadingState = GreetingUiState(displayValue = "Loading...", isLoading = true, error = null)

        coEvery {
            mockProcessor.processAction(any(), action)
        } returns flow {
            emit(loadingState)
        }

        // Act
        viewModel.onAction(action)
        advanceUntilIdle()

        // Assert
        assertEquals(loadingState, viewModel.uiState.value)
        coVerify(exactly = 1) { mockProcessor.processAction(any(), action) }
    }

    @Test
    fun `onAction should update state through multiple emissions from processor`() = runTest {
        // Arrange
        val action = GreetingScreenAction.RetrieveData
        val loadingState = GreetingUiState(displayValue = "Loading...", isLoading = true, error = null)
        val successState = GreetingUiState(displayValue = "42", isLoading = false, error = null)

        coEvery {
            mockProcessor.processAction(any(), action)
        } returns flow {
            emit(loadingState)
            emit(successState)
        }

        // Act
        viewModel.onAction(action)
        advanceUntilIdle()

        // Assert - should have the final state
        assertEquals(successState, viewModel.uiState.value)
        assertEquals("42", viewModel.uiState.value.displayValue)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `onAction should handle error state from processor`() = runTest {
        // Arrange
        val action = GreetingScreenAction.RetrieveData
        val loadingState = GreetingUiState(displayValue = "Loading...", isLoading = true, error = null)
        val errorState = GreetingUiState(displayValue = "Hello", isLoading = false, error = "Network error")

        coEvery {
            mockProcessor.processAction(any(), action)
        } returns flow {
            emit(loadingState)
            emit(errorState)
        }

        // Act
        viewModel.onAction(action)
        advanceUntilIdle()

        // Assert
        assertEquals(errorState, viewModel.uiState.value)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Network error", viewModel.uiState.value.error)
    }

    @Test
    fun `onAction should pass current state to processor`() = runTest {
        // Arrange
        val action = GreetingScreenAction.RetrieveData
        val expectedInitialState = GreetingUiState() // Default state

        coEvery {
            mockProcessor.processAction(expectedInitialState, action)
        } returns flow {
            emit(GreetingUiState(displayValue = "Result", isLoading = false, error = null))
        }

        // Act
        viewModel.onAction(action)
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { mockProcessor.processAction(expectedInitialState, action) }
    }

    @Test
    fun `multiple onAction calls should process sequentially`() = runTest {
        // Arrange
        val action = GreetingScreenAction.RetrieveData
        val state1 = GreetingUiState(displayValue = "First", isLoading = false, error = null)
        val state2 = GreetingUiState(displayValue = "Second", isLoading = false, error = null)

        coEvery {
            mockProcessor.processAction(any(), action)
        } returnsMany listOf(
            flow { emit(state1) },
            flow { emit(state2) }
        )

        // Act
        viewModel.onAction(action)
        advanceUntilIdle()
        viewModel.onAction(action)
        advanceUntilIdle()

        // Assert
        assertEquals(state2, viewModel.uiState.value)
        coVerify(exactly = 2) { mockProcessor.processAction(any(), action) }
    }

    @Test
    fun `onCleared should call processor with ClearData action`() = runTest {
        // Arrange
        val clearAction = GreetingScreenAction.ClearData
        coEvery {
            mockProcessor.processAction(any(), clearAction)
        } returns flow {
            // Empty flow for clear data action
        }

        // Act
        viewModel.onCleared()
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { mockProcessor.processAction(any(), clearAction) }
    }

    @Test
    fun `onCleared should pass current state to processor with ClearData action`() = runTest {
        // Arrange
        val currentState = viewModel.uiState.value
        val clearAction = GreetingScreenAction.ClearData

        coEvery {
            mockProcessor.processAction(currentState, clearAction)
        } returns flow {
            // Empty flow for clear data action
        }

        // Act
        viewModel.onCleared()
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { mockProcessor.processAction(currentState, clearAction) }
    }

    @Test
    fun `onCleared should handle flow from processor with multiple emissions`() = runTest {
        // Arrange
        val clearAction = GreetingScreenAction.ClearData
        val expectedState = GreetingUiState(displayValue = "Cleared", isLoading = false, error = null)

        coEvery {
            mockProcessor.processAction(any(), clearAction)
        } returns flow {
            emit(expectedState)
        }

        // Act
        viewModel.onCleared()
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { mockProcessor.processAction(any(), clearAction) }
    }
}

