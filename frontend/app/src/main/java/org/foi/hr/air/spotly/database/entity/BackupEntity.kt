package org.foi.hr.air.spotly.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "Korisnik")
@Serializable
data class Korisnik(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id") val ID: Int = 0,
    @SerialName("ime") val Ime: String,
    @SerialName("prezime") val Prezime: String,
    @SerialName("email") val Email: String,
    @SerialName("lozinka") val Lozinka: String,
    @SerialName("brojMobitela") val BrojMobitela: String,
    @SerialName("status") val Status: String,
    @SerialName("tipKorisnikaId") val TipKorisnikaId: Int
)

@Entity(tableName = "Tip_korisnika")
@Serializable
data class Tip_korisnika(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id") val ID: Int = 0,
    @SerialName("tip") val Tip: String,
)

@Entity(tableName = "Vozilo")
data class Vozilo(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val RegistracijskaOznaka: String,
    val Marka: String,
    val Model: String,
    val GodinaProizvodnje: Int,
    val KorisnikID: Int
)


@Entity(tableName = "Dokumentacija")
data class Dokumentacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Naziv: String,
    val Sadrzaj: String,
    val DatumKreiranja: String,
    val KorisnikID: Int
)

@Entity(tableName = "Kazna")
data class Kazna(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Iznos: Double,
    val Opis: String?,
    val Datum: String,
    val KorisnikID: Int,
    val VoziloID: Int?
)


@Entity(tableName = "Notifikacija")
data class Notifikacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Poruka: String,
    val Datum_vrijeme: String,
    val Tip_notifikacijeID: Int?,
    val KorisnikID: Int?
)

@Entity(tableName = "Parking_mjesto")
data class ParkingMjesto(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Status: String,
    val Dostupnost: String?,
    val Tip_mjestaID: Int?
)

@Entity(tableName = "Rezervacija")
data class Rezervacija(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Datum_vrijeme_rezervacije: String,
    val Datum_vrijeme_odlaska: String?,
    val VoziloID: Int?,
    val Parking_mjestoID: Int?
)

@Entity(tableName = "Tip_notifikacije")
data class TipNotifikacije(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "Tip_mjesta")
data class TipMjesta(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "Tip_vozila")
data class TipVozila(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val Tip: String
)

@Entity(tableName = "Zahtjev")
data class Zahtjev(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val TipZahtjeva: String,
    val Opis: String?,
    val Datum: String,
    val KorisnikID: Int,
    val Status: String
)
