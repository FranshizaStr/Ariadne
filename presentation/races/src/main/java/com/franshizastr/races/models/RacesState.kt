package com.franshizastr.races.models

import com.franshizastr.ErrorVO

data class RacesState(
    val races: List<RaceVO> = emptyList(),
    val error: ErrorVO? = null
)