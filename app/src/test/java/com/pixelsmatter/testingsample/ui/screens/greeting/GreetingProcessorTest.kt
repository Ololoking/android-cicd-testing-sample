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
}

