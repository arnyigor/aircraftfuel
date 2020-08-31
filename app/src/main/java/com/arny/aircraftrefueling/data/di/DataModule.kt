package com.arny.aircraftrefueling.data.di

import com.arny.aircraftrefueling.data.repository.files.FilesRepository
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.data.repository.units.UnitsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {
    @Binds
    @Singleton
    fun bindsRepository(filesRepository: FilesRepository): IFilesRepository
    @Binds
    @Singleton
    fun bindsUnitsRepository(unitsRepository: UnitsRepository): IUnitsRepository
}
