package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Zahtjev

@Dao
interface ZahtjevDao {
    @Query("SELECT * FROM Zahtjev")
    suspend fun getAll(): List<Zahtjev>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Zahtjev>)

    @Query("DELETE FROM Zahtjev")
    suspend fun deleteAll()
}