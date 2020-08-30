package com.arny.aircraftrefueling.data.di

import com.arny.aircraftrefueling.data.repository.FilesRepository
import com.arny.aircraftrefueling.data.repository.IFilesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {
    @Binds
    @Singleton
    fun bindsRepository(filesRepository: FilesRepository): IFilesRepository
}
