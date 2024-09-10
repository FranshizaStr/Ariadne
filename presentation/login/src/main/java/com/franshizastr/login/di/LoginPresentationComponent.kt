package com.franshizastr.login.di

import com.franshizastr.login.repositories.TeamsRepository
import dagger.Component

interface LoginPresentationDeps {

    fun teamsRepo(): TeamsRepository
}

@Component(
    dependencies = [
        LoginPresentationDeps::class
    ]
)
interface LoginPresentationComponent
