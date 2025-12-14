package com.pixelsmatter.data.repository

import com.pixelsmatter.data.datasource.VeryImportantLocalDataSource
import com.pixelsmatter.domain.VeryImportantRepository
import javax.inject.Inject

class VeryImportantRepositoryImpl @Inject constructor(
    private val localDataSource: VeryImportantLocalDataSource
) : VeryImportantRepository {

    override suspend fun retrieveData(): Int {
        return localDataSource.retrieveData()
    }
}