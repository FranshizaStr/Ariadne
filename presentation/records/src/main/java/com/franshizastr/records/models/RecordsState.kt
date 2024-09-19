package com.franshizastr.records.models

import com.franshizastr.ErrorVO

data class RecordsState(
    val records: List<RecordVO> = emptyList(),
    val error: ErrorVO? = null
)
