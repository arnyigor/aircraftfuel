package com.arny.aircraftrefueling.data.di

import android.content.Context
import com.arny.aircraftrefueling.data.repository.Prefs
import com.arny.aircraftrefueling.data.repository.files.FilesRepository
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.data.repository.units.UnitsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {
    companion object {
        @Provides
        fun providePrefs(context: Context): Prefs = Prefs.getInstance(context)
    }

    @Binds
    @Singleton
    fun bindsRepository(filesRepository: FilesRepository): IFilesRepository

    @Binds
    @Singleton
    fun bindsUnitsRepository(unitsRepository: UnitsRepository): IUnitsRepository
}
