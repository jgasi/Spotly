package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Korisnik

@Dao
interface KorisnikDao {
    @Query("SELECT * FROM Korisnik")
    suspend fun getAll(): List<Korisnik>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Korisnik>)

    @Query("DELETE FROM Korisnik")
    suspend fun deleteAll()
}