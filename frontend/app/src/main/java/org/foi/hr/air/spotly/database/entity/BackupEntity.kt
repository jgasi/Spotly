package org.foi.hr.air.spotly.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "backups")
data class BackupEntity(
    @PrimaryKey
    val timestamp: Long,
    val version: String = "",
    val description: String = ""
)

@Entity(tableName = "korisnik")
data class Korisnik(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Ime: String,
    val Prezime: String,
    val Email: String,
    val Lozinka: String,
    val Datum_registracije: String
)

@Entity(tableName = "korisnik_tip")
data class KorisnikTip(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Naziv: String,
    val Opis: String?
)

@Entity(tableName = "vozilo")
data class Vozilo(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val RegistracijskaOznaka: String,
    val Marka: String,
    val Model: String,
    val GodinaProizvodnje: Int,
    val KorisnikID: Int
)


@Entity(tableName = "dokumentacija")
data class Dokumentacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Naziv: String,
    val Sadrzaj: String,
    val DatumKreiranja: String,
    val KorisnikID: Int
)

@Entity(tableName = "kazna")
data class Kazna(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Iznos: Double,
    val Opis: String?,
    val Datum: String,
    val KorisnikID: Int,
    val VoziloID: Int?
)


@Entity(tableName = "notifikacija")
data class Notifikacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Poruka: String,
    val Datum_vrijeme: String,
    val Tip_notifikacijeID: Int?,
    val KorisnikID: Int?
)

@Entity(tableName = "parking_mjesto")
data class ParkingMjesto(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Status: String,
    val Dostupnost: String?,
    val Tip_mjestaID: Int?
)

@Entity(tableName = "rezervacija")
data class Rezervacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Datum_vrijeme_rezervacije: String,
    val Datum_vrijeme_odlaska: String?,
    val VoziloID: Int?,
    val Parking_mjestoID: Int?
)

@Entity(tableName = "tip_notifikacije")
data class TipNotifikacije(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "tip_mjesta")
data class TipMjesta(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "tip_vozila")
data class TipVozila(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "zahtjev")
data class Zahtjev(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val TipZahtjeva: String,
    val Opis: String?,
    val Datum: String,
    val KorisnikID: Int,
    val Status: String
)
