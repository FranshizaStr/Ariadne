package com.franshizastr.races.models

data class RaceVO(
    val id: String,
    val raceName: String,
    val teamId: String
)

fun RaceVO.map(): RaceModel {
    return with(this) {
        RaceModel(
            id = id,
            teamId = teamId,
            raceName = raceName
        )
    }
}

fun RaceModel.map(): RaceVO {
    return with(this) {
        RaceVO(
            id = id,
            teamId = teamId,
            raceName = raceName
        )
    }
}
