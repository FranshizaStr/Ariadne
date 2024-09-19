package com.franshizastr.records

import android.net.Uri
import com.franshizastr.CleanResult

interface AndroidFileWrite {
    fun launchNewWrite(
        fileName: String,
        newWriteToFileLambda: (uri: Uri?) -> Unit
    ): CleanResult<Unit>

    fun writeToFile(uri: Uri?)
}