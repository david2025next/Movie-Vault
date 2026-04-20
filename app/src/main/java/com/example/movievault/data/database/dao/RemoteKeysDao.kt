package com.example.movievault.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.movievault.data.database.model.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remotesKey : List<RemoteKeysEntity>)

    @Query(
        value = """
            SELECT * FROM remote_keys WHERE movieId =:movieId
        """
    )
    suspend fun remoteKeyMovieId(movieId : Int) : RemoteKeysEntity ?

    @Query(
        value = """
            DELETE FROM remote_keys
        """
    )
    suspend fun clearRemoteKeys()
}