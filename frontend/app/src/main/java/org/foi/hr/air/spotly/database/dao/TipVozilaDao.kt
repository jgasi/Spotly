package org.foi.hr.air.spotly.database.dao

import androidx.room.*
import org.foi.hr.air.spotly.database.entity.Tip_vozila

@Dao
interface TipVozilaDao {
    @Query("SELECT * FROM Tip_vozila")
    suspend fun getAll(): List<Tip_vozila>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(korisnici: List<Tip_vozila>)

    @Query("DELETE FROM Tip_vozila")
    suspend fun deleteAll()

    @Query("SELECT * FROM Tip_vozila WHERE ID = :id")
    suspend fun getById(id: Int): Tip_vozila?
}