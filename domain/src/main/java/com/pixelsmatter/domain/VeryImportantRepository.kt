package com.pixelsmatter.domain

interface VeryImportantRepository {
    suspend fun retrieveData(): Int
    suspend fun storeData(data: Long)
}