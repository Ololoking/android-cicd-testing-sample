package com.pixelsmatter.data.datasource

interface VeryImportantLocalDataSource {
    suspend fun retrieveData(): Int
    suspend fun clearData() : Int
}

