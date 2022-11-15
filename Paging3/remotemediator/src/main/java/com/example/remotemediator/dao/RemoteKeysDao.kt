package com.example.remotemediator.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.remotemediator.bean.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Long): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
