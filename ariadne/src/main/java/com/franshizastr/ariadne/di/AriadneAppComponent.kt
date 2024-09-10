package com.franshizastr.ariadne.di

import com.franshizastr.login.di.LoginDataModule
import dagger.Component

@Component(
    modules = [
        LoginDataModule::class
    ]
)
interface AriadneAppComponent {
}