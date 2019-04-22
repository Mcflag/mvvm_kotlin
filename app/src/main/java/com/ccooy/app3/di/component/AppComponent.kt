package com.ccooy.app3.di.component

import android.app.Application
import com.ccooy.app3.base.BaseApplication
import com.ccooy.app3.di.builder.ActivityBuilder
import com.ccooy.app3.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {

    fun inject(app: BaseApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}