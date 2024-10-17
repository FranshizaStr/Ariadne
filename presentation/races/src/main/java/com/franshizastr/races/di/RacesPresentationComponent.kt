package com.franshizastr.races.di

import com.franshizastr.races.repository.RacesRepository
import com.franshizastr.races.usecases.AddRaceUseCase
import com.franshizastr.races.usecases.EditRaceUseCase
import com.franshizastr.races.usecases.GetAllTeamRacesUseCase
import com.franshizastr.races.viewModel.RacesViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

interface RacesDeps {

    fun racesRepository(): RacesRepository
}

@RacesPresentationScope
@Component(
    modules = [
        RacesVMModule::class
    ],
    dependencies = [
        RacesDeps::class
    ]
)
interface RacesPresentationComponent {

    fun getRacesViewModel(): RacesViewModel

    @Component.Builder
    interface Builder {

        fun repositoryDeps(racesDeps: RacesDeps): Builder

        @BindsInstance
        fun teamId(@Named("teamId") teamId: String): Builder

        @BindsInstance
        fun teamName(@Named("teamName") teamName: String): Builder

        @BindsInstance
        fun navigationCallback(
            onRaceSnippetClick: (teamId: String, teamName: String, raceId: String, raceName: String) -> Unit
        ): Builder

        fun build(): RacesPresentationComponent
    }
}

@Module
class RacesVMModule {

    @RacesPresentationScope
    @Provides
    fun provideRacesViewModel(
        getAllTeamRacesUseCase: GetAllTeamRacesUseCase,
        addRaceUseCase: AddRaceUseCase,
        editRaceUseCase: EditRaceUseCase,
        onRaceSnippetClick: (teamId: String, teamName: String, raceId: String, raceName: String) -> Unit,
        @Named("teamId") teamId: String,
        @Named("teamName") teamName: String
    ): RacesViewModel {
        return RacesViewModel(
            getAllTeamRacesUseCase = getAllTeamRacesUseCase,
            teamName = teamName,
            teamId = teamId,
            addRaceUseCase = addRaceUseCase,
            onRaceSnippetClick = onRaceSnippetClick,
            editRaceUseCase = editRaceUseCase
        )
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RacesPresentationScope