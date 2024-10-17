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

    override fun getAllRecordsByTeamId(teamId: String, raceId: String): CleanResult<Flow<List<RecordModel>>> {
        return safelyExecuteDbOperation(
            entity = Pair(teamId, raceId),
            operationDescription = " observing records from DB"
        ) { entity ->
            entity!!
            val records = dao.getAllTeamRecords(
                teamId = entity.first,
                raceId = entity.second
            ).map { models ->
                models.map { model ->
                    model.map()
                }
            }
            CleanResult.Success(records)
        }
    }

    override suspend fun removeTeamRecords(teamId: String, raceId: String): CleanResult<Unit> {
        return safelyExecuteSuspendableDbOperation(
            entity = Pair(teamId, raceId),
            operationDispatcher = ioDispatcher,
            operationDescription = " removing records from DB"
        ) { entity ->
            entity!!
            dao.deleteTeamRecords(
                teamId = entity.first,
                raceId = entity.second
            )
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