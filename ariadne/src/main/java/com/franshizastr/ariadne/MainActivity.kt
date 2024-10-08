package com.franshizastr.ariadne

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.franshizastr.records.AndroidFileWriter
import com.franshizastr.records.di.DaggerRecordsPresentationComponent
import com.franshizastr.records.di.RecordsPresentationComponent
import com.franshizastr.records.navigation.RecordsScreen
import com.franshizastr.records.ui.RecordsScreen
import com.franshizastr.records.viewModel.RecordsViewModel

class MainActivity : ComponentActivity(), AndroidFileWriter {

    private var loginDiComponent: LoginPresentationComponent? = null
    private var loginViewModel: LoginViewModel? = null
    private var recordsDiComponent: RecordsPresentationComponent? = null
    private var recordsViewModel: RecordsViewModel? = null
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
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
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
                        recordsViewModel = null
                        recordsDiComponent = null
                        loginDiComponent = DaggerLoginPresentationComponent
                            .builder()
                            .repositoryDeps(this@MainActivity.appComponent)
                            .navigationCallback { teamId, teamName ->
                                navController.navigate(
                                    RecordsScreen(teamId, teamName)
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
                    composable<RecordsScreen> {
                        val args = it.toRoute<RecordsScreen>()
                        loginDiComponent = null
                        loginViewModel = null
                        recordsDiComponent = DaggerRecordsPresentationComponent
                            .builder()
                            .recordsDeps(this@MainActivity.appComponent)
                            .teamId(args.teamId)
                            .teamName(args.teamName)
                            .build()
                        recordsDiComponent?.let { component ->
                            recordsViewModel = daggerViewModel {
                                component.getRecordsViewModel()
                            }
                            EnterAnimation {
                                recordsViewModel?.let { viewModel ->
                                    RecordsScreen(viewModel, args.teamName)
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
    }
}
