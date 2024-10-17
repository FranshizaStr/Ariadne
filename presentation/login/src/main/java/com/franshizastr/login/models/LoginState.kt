package com.franshizastr.login.models

import com.franshizastr.ErrorVO

data class LoginState(
    val teams: List<TeamVO> = emptyList(),
    val error: ErrorVO? = null
)