package org.foi.hr.air.spotly.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.foi.hr.air.spotly.database.dao.BackupDao
import org.foi.hr.air.spotly.database.entity.BackupEntity

@Database(entities = [BackupEntity::class], version = 1)
abstract class BackupDatabase : RoomDatabase() {
    abstract fun backupDao(): BackupDao
}