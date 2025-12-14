package com.pixelsmatter.data.di

import com.pixelsmatter.data.datasource.VeryImportantLocalDataSource
import com.pixelsmatter.data.datasource.VeryImportantLocalDataSourceImpl
import com.pixelsmatter.data.repository.VeryImportantRepositoryImpl
import com.pixelsmatter.domain.VeryImportantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindVeryImportantLocalDataSource(
        veryImportantLocalDataSource: VeryImportantLocalDataSourceImpl
    ): VeryImportantLocalDataSource


    @Binds
    @Singleton
    fun bindVeryImportantRepository(
        veryImportantRepository: VeryImportantRepositoryImpl
    ): VeryImportantRepository
}

