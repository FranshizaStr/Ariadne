package com.franshizastr.ariadne

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.franshizastr.ariadne.application.appComponent
import com.franshizastr.daggerViewModel
import com.franshizastr.designsystem.theme.AriadneTheme
import com.franshizastr.login.di.DaggerLoginPresentationComponent
import com.franshizastr.login.di.LoginPresentationComponent
import com.franshizastr.login.navigation.LoginScreen
import com.franshizastr.login.ui.LoginScreen
import com.franshizastr.login.viewModel.LoginViewModel

class MainActivity : ComponentActivity(), FileWriter {

    override fun writeToFile(
        fileName: String,
        writeToFileLambda: (uri: Uri?) -> Unit
    ) {
        registerForActivityResult(
            ActivityResultContracts.CreateDocument("text/csv"),
            writeToFileLambda
        ).launch("Downloads/$fileName")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var loginDiComponent: LoginPresentationComponent? = null
        var loginViewModel: LoginViewModel? = null

        setContent {
            AriadneTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = LoginScreen,
                ) {
                    loginDiComponent = DaggerLoginPresentationComponent
                        .builder()
                        .repositoryDeps(this@MainActivity.appComponent)
                        .navigationCallback { teamId ->

                        }
                        .build()
                    loginViewModel = loginDiComponent?.getLoginViewModel()
                    loginViewModel?.let { viewModel ->
                        composable<LoginScreen> {
                            LoginScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

interface FileWriter {
    fun writeToFile(
        fileName: String,
        writeToFileLambda: (uri: Uri?) -> Unit
    )
}
