package com.franshizastr.ariadne

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.franshizastr.CleanResult
import com.franshizastr.ariadne.application.appComponent
import com.franshizastr.designsystem.EnterAnimation
import com.franshizastr.designsystem.daggerViewModel
import com.franshizastr.designsystem.theme.AriadneTheme
import com.franshizastr.login.di.DaggerLoginPresentationComponent
import com.franshizastr.login.di.LoginPresentationComponent
import com.franshizastr.login.navigation.LoginScreen
import com.franshizastr.login.ui.LoginScreen
import com.franshizastr.login.viewModel.LoginViewModel
import com.franshizastr.core.contextInterfaces.AndroidFileWriterContextInterface
import com.franshizastr.races.di.DaggerRacesPresentationComponent
import com.franshizastr.races.di.RacesPresentationComponent
import com.franshizastr.races.navigation.RaceScreenArgs
import com.franshizastr.races.ui.RacesScreen
import com.franshizastr.races.viewModel.RacesViewModel
import com.franshizastr.records.di.DaggerRecordsPresentationComponent
import com.franshizastr.records.di.RecordsPresentationComponent
import com.franshizastr.records.navigation.RecordsScreenArgs
import com.franshizastr.records.ui.RecordsScreen
import com.franshizastr.records.viewModel.RecordsViewModel

class MainActivity :
    ComponentActivity(),
    AndroidFileWriterContextInterface
{

    private var loginDiComponent: LoginPresentationComponent? = null
    private var loginViewModel: LoginViewModel? = null
    private var recordsDiComponent: RecordsPresentationComponent? = null
    private var recordsViewModel: RecordsViewModel? = null
    private var racesDiComponent: RacesPresentationComponent? = null
    private var racesViewModel: RacesViewModel? = null
    private var writeToFileLambda: (uri: Uri?) -> Unit = { }
    private lateinit var resultLauncher: ActivityResultLauncher<String>

    override fun writeToFile(uri: Uri?) {
        writeToFileLambda(uri)
    }

    override fun launchNewWrite(
        fileName: String,
        newWriteToFileLambda: (uri: Uri?) -> Unit
    ): CleanResult<Unit> {

        return try {
            writeToFileLambda = newWriteToFileLambda
            resultLauncher.launch(fileName)
            CleanResult.Success(Unit)
        } catch (ex: Throwable) {
            CleanResult.Failure(
                error = CleanResult.Error(
                    previousError = null,
                    throwable = null,
                    message = "error happened while writing to a file",
                    level = CleanResult.Error.ErrorLevel.DOMAIN
                )
            )
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {

        @Composable
        fun navigateToRacesScreen(
            navController: NavHostController
        ) {
            recordsViewModel = null
            recordsDiComponent = null
            racesViewModel = null
            racesDiComponent = null
            loginDiComponent = DaggerLoginPresentationComponent
                .builder()
                .repositoryDeps(this@MainActivity.appComponent)
                .navigationCallback { teamId, teamName ->
                    navController.navigate(
                        RaceScreenArgs(
                            teamId = teamId,
                            teamName = teamName
                        )
                    )
                }
                .build()
            loginDiComponent?.let { component ->
                loginViewModel = daggerViewModel {
                    component.getLoginViewModel()
                }
                loginViewModel?.let { viewModel ->
                    EnterAnimation {
                        LoginScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val permissions = listOfNotNull(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.POST_NOTIFICATIONS
            } else {
                null
            }
        )

        ActivityCompat.requestPermissions(
            this@MainActivity,
            permissions.toTypedArray(),
            1
        )

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.CreateDocument("text/csv"),
            this::writeToFile
        )

        enableEdgeToEdge()

        setContent {
            AriadneTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = LoginScreen,
                ) {
                    composable<LoginScreen> {
                        navigateToRacesScreen(navController)
                    }
                    composable<RaceScreenArgs> {
                        val args = it.toRoute<RaceScreenArgs>()
                        recordsViewModel = null
                        recordsDiComponent = null
                        racesDiComponent = DaggerRacesPresentationComponent
                            .builder()
                            .repositoryDeps(this@MainActivity.appComponent)
                            .teamId(args.teamId)
                            .teamName(args.teamName)
                            .navigationCallback { teamId, teamName, raceId, raceName ->
                                navController.navigate(
                                    RecordsScreenArgs(
                                        teamName = teamName,
                                        teamId = teamId,
                                        raceId = raceId,
                                        raceName = raceName
                                    )
                                )
                            }
                            .build()
                        racesDiComponent?.let { component ->
                            racesViewModel = daggerViewModel {
                                component.getRacesViewModel()
                            }
                            racesViewModel?.let { viewModel ->
                                EnterAnimation {
                                    RacesScreen(viewModel)
                                }
                            }
                        }
                    }
                    composable<RecordsScreenArgs> {
                        val args = it.toRoute<RecordsScreenArgs>()
                        recordsDiComponent = DaggerRecordsPresentationComponent
                            .builder()
                            .recordsDeps(this@MainActivity.appComponent)
                            .teamId(args.teamId)
                            .teamName(args.teamName)
                            .raceId(args.raceId)
                            .raceName(args.raceName)
                            .build()
                        recordsDiComponent?.let { component ->
                            recordsViewModel = daggerViewModel {
                                component.getRecordsViewModel()
                            }
                            EnterAnimation {
                                recordsViewModel?.let { viewModel ->
                                    RecordsScreen(viewModel, args.teamName, args.raceName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginDiComponent = null
        loginViewModel = null
        recordsDiComponent = null
        recordsViewModel = null
        racesViewModel = null
        racesDiComponent = null
    }
}
