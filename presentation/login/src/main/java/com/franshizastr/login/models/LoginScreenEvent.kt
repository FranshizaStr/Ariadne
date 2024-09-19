package com.franshizastr.login.models

sealed class LoginScreenEvent {
    class LongTapOnTeamEvent(val teamId: String, val teamName: String): LoginScreenEvent()
    class OnNewTeamSelectEvent(val teamName: String): LoginScreenEvent()
    class OnOldTeamSelectEvent(val teamId: String, val teamName: String): LoginScreenEvent()
    data object OnErrorEventShown: LoginScreenEvent()
}