package com.franshizastr.records.repositories

import com.franshizastr.CleanResult
import com.franshizastr.records.database.RecordsDao
import com.franshizastr.records.database.map
import com.franshizastr.records.models.RecordModel
import com.franshizastr.safelyExecuteDbOperation
import com.franshizastr.safelyExecuteSuspendableDbOperation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val dao: RecordsDao,
    private val ioDispatcher: CoroutineDispatcher
) : RecordRepository {

    override fun getAllRecordsByTeamId(teamId: String): CleanResult<Flow<List<RecordModel>>> {
        return safelyExecuteDbOperation(
            entity = teamId,
            operationDescription = " observing records from DB"
        ) { entity ->
            val records = dao.getAllTeamRecords(entity!!).map { models ->
                models.map { model ->
                    model.map()
                }
            }
            CleanResult.Success(records)
        }
    }

    override suspend fun removeTeamRecords(teamId: String): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = teamId,
            operationDispatcher = ioDispatcher,
            operationDescription = " removing records from DB"
        ) { entity ->
            dao.deleteTeamRecords(entity!!)
            CleanResult.Success(Unit)
        }
    }

    override suspend fun saveRecord(record: RecordModel): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = record.map(),
            operationDispatcher = ioDispatcher,
            operationDescription = " adding new records to DB"
        ) { entity ->
            dao.insertRecord(entity!!)
            CleanResult.Success(Unit)
        }
    }
}