package com.franshizastr.ariadne.di

import com.franshizastr.login.di.LoginDataModule
import com.franshizastr.login.di.LoginPresentationDeps
import dagger.Component

@Component(
    modules = [
        LoginDataModule::class
    ]
)
interface AriadneAppComponent : LoginPresentationDeps {

    @Component.Builder
    interface Builder {

        fun build(): AriadneAppComponent
    }
}
