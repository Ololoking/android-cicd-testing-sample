package com.pixelsmatter.data.datasource

import javax.inject.Inject

/**
 * Interface for local data source (database)
 * Defines contract for accessing VeryImportant data locally
 */
class VeryImportantLocalDataSourceImpl @Inject constructor(
) : VeryImportantLocalDataSource {

    private var counter = 1

    override suspend fun retrieveData(): Int {
        return counter++ * MULTIPLIER
    }

    override suspend fun clearData(): Int {
        counter = 1
        return counter
    }

    companion object {
        private const val MULTIPLIER = 10 * 4 * 2
    }

}

