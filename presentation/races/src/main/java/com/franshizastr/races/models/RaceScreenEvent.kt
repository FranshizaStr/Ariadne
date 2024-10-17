package com.franshizastr.races.models

sealed class RaceScreenEvent {
    class LongTapOnRaceEvent(val raceModel: RaceModel, val raceName: String): RaceScreenEvent()
    class OnNewRaceSelectEvent(val raceName: String): RaceScreenEvent()
    class OnOldRaceSelectEvent(val raceId: String, val raceName: String): RaceScreenEvent()
    data object OnErrorEventShown: RaceScreenEvent()
}