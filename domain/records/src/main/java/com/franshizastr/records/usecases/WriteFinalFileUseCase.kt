package com.franshizastr.records.usecases

import com.franshizastr.CleanResult
import com.franshizastr.ContextProvider
import com.franshizastr.core.contextInterfaces.AndroidFileWriterContextInterface
import com.franshizastr.records.models.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WriteFinalFileUseCase @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun execute(
        records: List<RecordModel>,
        fileName: String,
        fileWriter: AndroidFileWriterContextInterface
    ): CleanResult<Unit> {
        val context = contextProvider.context
        return withContext(dispatcher) {
            try {
                fileWriter.launchNewWrite(fileName) { uri ->
                    uri ?: throw Exception("uri is null for some reason")
                    context.contentResolver.openOutputStream(uri)?.writer()?.run {
                        write("latitude, longitude, altitude, timestamp\r\n")
                        records.forEach { record ->
                            write("${record.latitude}, ${record.longitude}, ${record.altitude}, ${record.time}\r\n")
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
