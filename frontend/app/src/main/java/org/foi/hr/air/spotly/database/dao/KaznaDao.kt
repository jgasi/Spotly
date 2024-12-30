package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Kazna

@Dao
interface KaznaDao {
    @Query("SELECT * FROM Kazna")
    suspend fun getAll(): List<Kazna>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Kazna>)

    @Query("DELETE FROM Kazna")
    suspend fun deleteAll()
}