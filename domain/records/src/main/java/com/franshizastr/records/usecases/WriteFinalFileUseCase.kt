package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.ContextProvider
import com.franshizastr.records.AndroidFileWrite
import com.franshizastr.records.models.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WriteFinalFileUseCase @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dispatcher: CoroutineDispatcher,
    private val fileWriter: AndroidFileWrite,
) {

    suspend fun execute(
        records: List<RecordModel>,
        fileName: String
    ): CleanResult<Unit> {
        val context = contextProvider.context
        return withContext(dispatcher) {
            try {
                fileWriter.launchNewWrite(fileName) { uri ->
                    uri ?: throw Exception("uri is null for some reason")
                    context.contentResolver.openOutputStream(uri)?.writer()?.run {
                        records.forEach { record ->
                            write("${record.latitude}, ${record.longitude}, ${record.time}\r\n")
                        }
                        flush()
                        close()
                    }
                    CleanResult.Success(Unit)
                }
            } catch (ex: Throwable) {
                CleanResult.Failure(
                    error = CleanResult.Error(
                        previousError = null,
                        message = "error while writing to file - IO error",
                        throwable = ex,
                        level = CleanResult.Error.ErrorLevel.DOMAIN
                    )
                )
            }
        }
    }
}