package com.pixelsmatter.data.datasource

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class VeryImportantLocalDataSourceImplTest {

    @Test
    fun `retrieveData increments and multiplies`() = runBlocking {
        val ds = VeryImportantLocalDataSourceImpl()

        val first = ds.retrieveData()
        // MULTIPLIER = 10 * 4 * 2 = 80
        assertEquals(80, first)

        val second = ds.retrieveData()
        assertEquals(160, second)

        val third = ds.retrieveData()
        assertEquals(240, third)
    }

    @Test
    fun `instances are independent`() = runBlocking {
        val a = VeryImportantLocalDataSourceImpl()
        val b = VeryImportantLocalDataSourceImpl()

        assertEquals(80, a.retrieveData())
        assertEquals(80, b.retrieveData())

        assertEquals(160, a.retrieveData())
        assertEquals(160, b.retrieveData())
    }

    @Test
    fun `clearData resets counter to 1`() = runBlocking {
        val ds = VeryImportantLocalDataSourceImpl()

        // Increment counter multiple times
        ds.retrieveData() // counter becomes 2
        ds.retrieveData() // counter becomes 3
        ds.retrieveData() // counter becomes 4

        // Clear and verify
        val cleared = ds.clearData()
        assertEquals(1, cleared)
    }

    @Test
    fun `clearData allows retrieval to start over`() = runBlocking {
        val ds = VeryImportantLocalDataSourceImpl()

        // Use data source
        ds.retrieveData() // counter becomes 2
        ds.retrieveData() // counter becomes 3

        // Clear
        ds.clearData()

        // Should start from 1 again
        val first = ds.retrieveData()
        assertEquals(80, first)

        val second = ds.retrieveData()
        assertEquals(160, second)
    }
}

