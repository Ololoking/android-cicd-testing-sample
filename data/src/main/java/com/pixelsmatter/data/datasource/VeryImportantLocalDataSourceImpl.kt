package com.pixelsmatter.data.datasource

import javax.inject.Inject

/**
 * Interface for local data source (database)
 * Defines contract for accessing VeryImportant data locally
 */
class VeryImportantLocalDataSourceImpl @Inject constructor(
) : VeryImportantLocalDataSource {

    private var retrieveCounter = 1
    private var screenLoadAtTime = 0L

    override suspend fun retrieveData(): Int {
        return retrieveCounter++ * MULTIPLIER
    }

    override suspend fun storeData(data: Long) {
        screenLoadAtTime = data
    }

    companion object {
        private const val MULTIPLIER = 10 * 4 * 2
    }

}

