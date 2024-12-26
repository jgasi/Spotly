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
        Tip_korisnika::class,
        Korisnik::class,
        Vozilo::class,
        Zahtjev::class,
        Notifikacija::class,
        ParkingMjesto::class,
        Rezervacija::class,
        TipNotifikacije::class,
        TipMjesta::class,
        Tip_vozila::class
    ],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun korisnikDao(): KorisnikDao
    abstract fun dokumentacijaDao(): DokumentacijaDao
    abstract fun voziloDao(): VoziloDao
    abstract fun tipVozilaDao(): TipVozilaDao
    abstract fun tipKorisnikaDao(): TipKorisnikaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spotly-db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also {INSTANCE = it}
            }
        }
    }
}