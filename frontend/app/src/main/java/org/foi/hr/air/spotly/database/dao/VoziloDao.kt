package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Vozilo

@Dao
interface VoziloDao {
    @Query("SELECT * FROM Vozilo")
    suspend fun getAll(): List<Vozilo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Vozilo>)

    @Query("DELETE FROM Vozilo")
    suspend fun deleteAll()
}