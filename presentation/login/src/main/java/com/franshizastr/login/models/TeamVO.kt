package com.franshizastr.login.models

data class TeamVO(
    val id: String,
    val teamName: String
)

fun TeamVO.map(): TeamModel {
    return with(this) {
        TeamModel(
            id = id,
            teamName = teamName
        )
    }
}

fun TeamModel.map(): TeamVO {
    return with(this) {
        TeamVO(
            id = id,
            teamName = teamName
        )
    }
}
