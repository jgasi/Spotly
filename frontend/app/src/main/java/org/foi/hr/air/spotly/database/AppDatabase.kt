package org.foi.hr.air.spotly.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.foi.hr.air.spotly.database.dao.*
import org.foi.hr.air.spotly.database.entity.*

@Database(
    entities = [
        Dokumentacija::class,
        Kazna::class,
        KorisnikTip::class,
        Korisnik::class,
        Vozilo::class,
        Zahtjev::class,
        Notifikacija::class,
        ParkingMjesto::class,
        Rezervacija::class,
        TipNotifikacije::class,
        TipMjesta::class,
        TipVozila::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun korisnikDao(): KorisnikDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spotly-db"
                ).build().also {INSTANCE = it}
            }
        }
    }
}