package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.data.di.DataModule
import com.arny.aircraftrefueling.domain.di.DomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        UiModule::class,
        DomainModule::class,
        DataModule::class,
    ]
)
interface AppComponent : AndroidInjector<RefuelApp> {
    override fun inject(application: RefuelApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: RefuelApp): Builder

        fun build(): AppComponent
    }
}