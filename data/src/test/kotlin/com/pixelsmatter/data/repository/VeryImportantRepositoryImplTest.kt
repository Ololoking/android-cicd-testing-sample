package com.pixelsmatter.data.repository

import com.pixelsmatter.data.datasource.VeryImportantLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VeryImportantRepositoryImplTest {

    private lateinit var mockLocalDataSource: VeryImportantLocalDataSource
    private lateinit var repository: VeryImportantRepositoryImpl

    @Before
    fun setup() {
        mockLocalDataSource = mockk()
        repository = VeryImportantRepositoryImpl(mockLocalDataSource)
    }

    @Test
    fun `retrieveData should return data from local source when available`() = runBlocking {
        // Arrange
        coEvery { mockLocalDataSource.retrieveData() } returns 42

        // Act
        val result = repository.retrieveData()

        // Assert
        assertEquals(42, result)
        coVerify(exactly = 1) { mockLocalDataSource.retrieveData() }
    }
}

