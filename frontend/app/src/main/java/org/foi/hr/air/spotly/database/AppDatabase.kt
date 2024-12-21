package org.foi.hr.air.spotly.database

import androidx.room.Database
import androidx.room.RoomDatabase
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
abstract class AppDatabase : RoomDatabase()