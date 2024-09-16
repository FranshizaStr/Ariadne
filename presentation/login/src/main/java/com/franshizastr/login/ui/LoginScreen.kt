package com.franshizastr.login.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.franshizastr.login.models.TeamVO
import com.franshizastr.login.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    LazyColumn {}
}

@Composable
fun OldTeamSnippet(
    teamVO: TeamVO
) {
    var isEditable by remember { mutableStateOf(false) }

}