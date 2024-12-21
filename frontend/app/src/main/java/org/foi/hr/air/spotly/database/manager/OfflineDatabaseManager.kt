package org.foi.hr.air.spotly.navigation.components

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.BackupDatabase
import org.foi.hr.air.spotly.database.entity.BackupEntity
import java.text.SimpleDateFormat
import java.util.*

class OfflineDatabaseManager private constructor(context: Context) {
    private val appDatabase: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "spotly-db"
    ).build()

    private val backupDatabase: BackupDatabase = Room.databaseBuilder(
        context,
        BackupDatabase::class.java, "spotly-backup-db"
    ).build()

    suspend fun setOfflineMode(enabled: Boolean) {
        withContext(Dispatchers.IO) {
            if (enabled) {
                copyDatabaseToBackup()
            }
        }
    }

    suspend fun getBackups(): List<DatabaseBackup> = withContext(Dispatchers.IO) {
        backupDatabase.backupDao().getAllBackups().map { backup ->
            DatabaseBackup(
                version = SimpleDateFormat("dd.MM.yyyy. Backup", Locale.getDefault())
                    .format(Date(backup.timestamp)),
                timestamp = backup.timestamp
            )
        }
    }

    suspend fun restoreBackup(timestamp: Long) = withContext(Dispatchers.IO) {
        backupDatabase.backupDao().getBackup(timestamp)?.let { backup ->
            appDatabase.clearAllTables()
        }
    }

    suspend fun syncWithRemote() = withContext(Dispatchers.IO) {
        createBackup()
    }

    private suspend fun createBackup() {
        val timestamp = System.currentTimeMillis()
        copyDatabaseToBackup()
        backupDatabase.backupDao().insertBackup(BackupEntity(timestamp))
    }

    private suspend fun copyDatabaseToBackup() {
    }

    companion object {
        @Volatile
        private var instance: OfflineDatabaseManager? = null

        fun getInstance(context: Context? = null): OfflineDatabaseManager {
            return instance ?: synchronized(this) {
                instance ?: OfflineDatabaseManager(context!!).also { instance = it }
            }
        }
    }
}