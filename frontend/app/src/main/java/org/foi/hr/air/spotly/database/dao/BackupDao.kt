package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.BackupEntity

@Dao
interface BackupDao {
    @Query("SELECT * FROM backups ORDER BY timestamp DESC")
    suspend fun getAllBackups(): List<BackupEntity>

    @Query("SELECT * FROM backups WHERE timestamp = :timestamp")
    suspend fun getBackup(timestamp: Long): BackupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackup(backup: BackupEntity)

    @Delete
    suspend fun deleteBackup(backup: BackupEntity)
}