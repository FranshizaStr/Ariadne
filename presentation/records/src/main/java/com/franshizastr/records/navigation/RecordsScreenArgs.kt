package com.franshizastr.records.navigation

import kotlinx.serialization.Serializable

@Serializable
data class RecordsScreenArgs(
    val teamId: String,
    val teamName: String,
    val raceId: String,
    val raceName: String
)
