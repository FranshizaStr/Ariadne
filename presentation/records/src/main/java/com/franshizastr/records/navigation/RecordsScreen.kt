package com.franshizastr.records.navigation

import kotlinx.serialization.Serializable

@Serializable
data class RecordsScreen(
    val teamId: String,
    val teamName: String
)
