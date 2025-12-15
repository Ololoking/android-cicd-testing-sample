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
    fun `storeData does not reset counter`() = runBlocking {
        val ds = VeryImportantLocalDataSourceImpl()

        val before = ds.retrieveData() // 80
        ds.storeData(12345L) // should not throw or reset counter
        val after = ds.retrieveData()
        assertEquals(160, after)
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
}

