package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Tip_korisnika

@Dao
interface TipKorisnikaDao {
    @Query("SELECT * FROM Tip_korisnika")
    suspend fun getAll(): List<Tip_korisnika>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Tip_korisnika>)

    @Query("DELETE FROM Tip_korisnika")
    suspend fun deleteAll()
}