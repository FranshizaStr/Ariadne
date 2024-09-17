package com.franshizastr.login.di

import com.franshizastr.login.repositories.TeamsRepository
import com.franshizastr.login.usecases.AddTeamUseCase
import com.franshizastr.login.usecases.EditTeamUseCase
import com.franshizastr.login.usecases.GetAllTeamsUseCase
import com.franshizastr.login.viewModel.LoginViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

interface RepositoryDeps {

    fun repository(): TeamsRepository
}

@LoginPresentationScope
@Component(
    modules = [
        LoginVMModule::class
    ],
    dependencies = [
        RepositoryDeps::class
    ]
)
interface LoginPresentationComponent {

    fun getLoginViewModel(): LoginViewModel

    @Component.Builder
    interface Builder {

        fun repositoryDeps(repositoryDeps: RepositoryDeps): Builder

        @BindsInstance
        fun navigationCallback(onTeamSnippetClick: (teamId: String) -> (Unit)): Builder

        fun build(): LoginPresentationComponent
    }
}

@Module
class LoginVMModule {

    @LoginPresentationScope
    @Provides
    fun provideLoginViewModel(
        getAllTeamsUseCase: GetAllTeamsUseCase,
        addTeamUseCase: AddTeamUseCase,
        editTeamUseCase: EditTeamUseCase,
        onTeamSnippetClick: (teamId: String) -> (Unit)
    ): LoginViewModel {
        return LoginViewModel(
            getAllTeamsUseCase,
            addTeamUseCase,
            editTeamUseCase,
            onTeamSnippetClick
        )
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginPresentationScope