package com.pixelsmatter.data.datasource

fun interface VeryImportantLocalDataSource {
    suspend fun retrieveData(): Int
}

