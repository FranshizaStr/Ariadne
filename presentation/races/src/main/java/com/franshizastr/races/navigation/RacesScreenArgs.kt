package com.franshizastr.races.navigation

import kotlinx.serialization.Serializable

@Serializable
data class RaceScreenArgs(
    val teamId: String,
    val teamName: String,
)