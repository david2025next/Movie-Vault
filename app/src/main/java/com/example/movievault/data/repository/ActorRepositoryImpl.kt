package com.example.movievault.data.repository

import android.util.Log
import com.example.movievault.data.database.dao.ActorDao
import com.example.movievault.data.database.model.toDomain
import com.example.movievault.data.model.Actor
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.asEntity
import com.example.movievault.data.utils.getDataError
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val movaNetworkDataSource: MovaNetworkDataSource,
    private val actorDao: ActorDao
) : ActorRepository {


    override fun getActorsForMovieWithId(movieId: Int): Flow<List<Actor>> =
        actorDao.getActorsForMovieIdStream(movieId)
            .map { actorEntities -> actorEntities.toDomain() }

    override suspend fun syncActors(movieId: Int): Result<Unit, DataErrors> {

        return if (actorDao.countActors(movieId) == 0L) {
            try {
                val actors = movaNetworkDataSource.getActorsForMovieWithId(movieId)
                    .map { networkActor -> networkActor.asEntity(movieId) }
                actorDao.insertAll(actors)
                Result.Success(Unit)
            } catch (ex: Exception) {
                val dataErrors = ex.getDataError()
                Result.Error(dataErrors)
            }
        } else {
            Result.Success(Unit)
        }
    }

}