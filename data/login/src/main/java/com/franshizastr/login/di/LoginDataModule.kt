package com.franshizastr.login.di

import com.franshizastr.login.repositories.TeamRepositoryImpl
import com.franshizastr.login.repositories.TeamsRepository
import dagger.Binds
import dagger.Module

@Module
interface LoginDataModule {

    @Binds
    fun bindRepository(repositoryImpl: TeamRepositoryImpl): TeamsRepository
}