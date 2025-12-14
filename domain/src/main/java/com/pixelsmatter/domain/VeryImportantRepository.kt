package com.pixelsmatter.domain

fun interface VeryImportantRepository {
    suspend fun retrieveData(): Int
}