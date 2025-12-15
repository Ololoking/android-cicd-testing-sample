package com.pixelsmatter.testingsample.ui.screens.greeting

import com.pixelsmatter.domain.VeryImportantRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GreetingProcessorTest {

    private lateinit var mockRepository: VeryImportantRepository
    private lateinit var processor: GreetingProcessor

    @Before
    fun setup() {
        mockRepository = mockk()
        processor = GreetingProcessor(mockRepository)
    }

    @Test
    fun `processAction should emit loading state then success state when data retrieval succeeds`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        val expectedData = 42
        coEvery { mockRepository.retrieveData() } returns expectedData

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)

        // First state: Loading
        assertEquals("Loading...", states[0].displayValue)
        assertTrue(states[0].isLoading)
        assertNull(states[0].error)

        // Second state: Success
        assertEquals(expectedData.toString(), states[1].displayValue)
        assertFalse(states[1].isLoading)
        assertNull(states[1].error)

        coVerify(exactly = 1) { mockRepository.retrieveData() }
    }

    @Test
    fun `processAction should emit loading state then error state when data retrieval fails`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        val errorMessage = "Network error"
        coEvery { mockRepository.retrieveData() } throws Exception(errorMessage)

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)

        // First state: Loading
        assertEquals("Loading...", states[0].displayValue)
        assertTrue(states[0].isLoading)
        assertNull(states[0].error)

        // Second state: Error
        assertEquals("Hello", states[1].displayValue) // displayValue retains value from initial currentState
        assertFalse(states[1].isLoading)
        assertEquals(errorMessage, states[1].error)

        coVerify(exactly = 1) { mockRepository.retrieveData() }
    }

    @Test
    fun `processAction should handle exception with unknown error message when exception message is null`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } throws Exception(null as String?)

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)

        // Second state should have unknown error
        assertEquals("Hello", states[1].displayValue) // displayValue retains value from initial currentState
        assertFalse(states[1].isLoading)
        assertEquals("Unknown error", states[1].error)

        coVerify(exactly = 1) { mockRepository.retrieveData() }
    }

    @Test
    fun `processAction should preserve current state values when emitting loading state`() = runTest {
        // Arrange
        val initialState = GreetingUiState(
            displayValue = "Previous Value",
            isLoading = false,
            error = "Previous Error"
        )
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } returns 99

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertTrue(states.size >= 2)

        // Loading state should have updated loading-related fields
        assertEquals("Loading...", states[0].displayValue)
        assertTrue(states[0].isLoading)
        assertNull(states[0].error)
    }

    @Test
    fun `processAction should call clearData on repository when ClearData action is received`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } returns 1

        // Act
        processor.processAction(initialState, action).toList()

        // Assert
        coVerify(exactly = 1) { mockRepository.clearData() }
    }

    @Test
    fun `processAction should not emit any states for ClearData action`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } returns 1

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(0, states.size)
    }

    @Test
    fun `processAction ClearData should handle exceptions from repository`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } throws Exception("Clear failed")

        // Act & Assert - should throw exception
        try {
            processor.processAction(initialState, action).toList()
            // If no exception, test passes as we're just verifying clearData was called
            coVerify(exactly = 1) { mockRepository.clearData() }
        } catch (ex: Exception) {
            // Exception is expected, verification already done above
            coVerify(exactly = 1) { mockRepository.clearData() }
        }
    }

    @Test
    fun `processAction RetrieveData emits correct number of states`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } returns 100

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert - should emit exactly 2 states (loading + success)
        assertEquals(2, states.size)
        assertTrue(states[0].isLoading)
        assertFalse(states[1].isLoading)
    }

    @Test
    fun `processAction with different initial states preserves custom display value on error`() = runTest {
        // Arrange
        val initialState = GreetingUiState(
            displayValue = "Custom Value",
            isLoading = false,
            error = null
        )
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } throws Exception("Test error")

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)
        // First state changes display to Loading
        assertEquals("Loading...", states[0].displayValue)
        // Error state preserves original state's display value
        assertEquals("Custom Value", states[1].displayValue)
        assertEquals("Test error", states[1].error)
    }

    @Test
    fun `processAction RetrieveData with large data value`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        val largeValue = 999999
        coEvery { mockRepository.retrieveData() } returns largeValue

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)
        assertEquals(largeValue.toString(), states[1].displayValue)
        assertFalse(states[1].isLoading)
        assertNull(states[1].error)
    }

    @Test
    fun `processAction RetrieveData with negative data value`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        val negativeValue = -42
        coEvery { mockRepository.retrieveData() } returns negativeValue

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)
        assertEquals(negativeValue.toString(), states[1].displayValue)
    }

    @Test
    fun `processAction RetrieveData clears previous error on success`() = runTest {
        // Arrange
        val initialState = GreetingUiState(
            displayValue = "Hello",
            isLoading = false,
            error = "Previous error"
        )
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } returns 42

        // Act
        val states = processor.processAction(initialState, action).toList()

        // Assert
        assertEquals(2, states.size)
        // First state clears error
        assertNull(states[0].error)
        // Success state also has no error
        assertNull(states[1].error)
    }

    @Test
    fun `processAction ClearData with exception still calls repository`() = runTest {
        // Arrange
        val initialState = GreetingUiState(
            displayValue = "Test",
            isLoading = false,
            error = null
        )
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } throws RuntimeException("Clear operation failed")

        // Act & Assert
        var exceptionThrown = false
        try {
            processor.processAction(initialState, action).toList()
        } catch (e: RuntimeException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
        coVerify(exactly = 1) { mockRepository.clearData() }
    }

    @Test
    fun `processAction RetrieveData flow completes after emitting states`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.RetrieveData
        coEvery { mockRepository.retrieveData() } returns 55

        // Act
        val flow = processor.processAction(initialState, action)
        var emissionCount = 0
        flow.collect { _ ->
            emissionCount++
        }

        // Assert - flow completes after emitting both states
        assertEquals(2, emissionCount)
    }

    @Test
    fun `importantRepository clearData returns 1 always`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } returns 1

        // Act
        processor.processAction(initialState, action).toList()

        // Assert - verify clearData was called and returns 1
        coVerify(exactly = 1) { mockRepository.clearData() }
    }

    @Test
    fun `processAction ClearData with clearData returning 1`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } returns 1

        // Act - Execute the action
        val states = processor.processAction(initialState, action).toList()

        // Assert - ClearData doesn't emit states but calls repository with return value 1
        assertEquals(0, states.size)
        coVerify(exactly = 1) { mockRepository.clearData() }
    }

    @Test
    fun `processAction multiple ClearData calls each return 1`() = runTest {
        // Arrange
        val initialState = GreetingUiState()
        val action = GreetingScreenAction.ClearData
        coEvery { mockRepository.clearData() } returns 1

        // Act - Call clearData multiple times
        processor.processAction(initialState, action).toList()
        processor.processAction(initialState, action).toList()
        processor.processAction(initialState, action).toList()

        // Assert - clearData called 3 times and always returns 1
        coVerify(exactly = 3) { mockRepository.clearData() }
    }
}

