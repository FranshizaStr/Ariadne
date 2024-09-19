package com.franshizastr.records.models

sealed class RecordsScreenEvent {
    data object SaveCSVFileWithRecords : RecordsScreenEvent()
    data object TakeNewRecord : RecordsScreenEvent()
    data object OnErrorEventShown: RecordsScreenEvent()
}