package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Dokumentacija

@Dao
interface DokumentacijaDao {
    @Query("SELECT * FROM Dokumentacija")
    suspend fun getAll(): List<Dokumentacija>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Dokumentacija>)

    @Query("DELETE FROM Dokumentacija")
    suspend fun deleteAll()
}