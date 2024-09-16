package com.franshizastr.ariadne.di

import android.content.Context
import com.franshizastr.core.di.DispatchersModule
import com.franshizastr.login.di.LoginDataModule
import com.franshizastr.login.di.LoginPresentationDeps
import com.franshizastr.login.di.LoginRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LoginDataModule::class,
        LoginRepositoryModule::class,
        DispatchersModule::class
    ]
)
interface AriadneAppComponent : LoginPresentationDeps {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AriadneAppComponent
    }
}

