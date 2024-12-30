package org.foi.hr.air.spotly.datastore

import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.data.UserType
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.entity.Korisnik
import org.foi.hr.air.spotly.database.entity.Tip_korisnika

class RoomUsersDataSource(private val db: AppDatabase) {
    suspend fun fetchUsers(): List<User> {
        val korisnici = db.korisnikDao().getAll()
        if (korisnici.isEmpty()) return emptyList()

        val users = korisnici.map { kor -> mapKorisnikToUser(kor) }
        return users
    }

    suspend fun fetchUserTypes(): Map<Int, String> {
        val tipoviKorisnika = db.tipKorisnikaDao().getAll()
        if (tipoviKorisnika.isEmpty()) return emptyMap()
        return tipoviKorisnika.associate { tip -> tip.ID to tip.Tip }
    }

    private fun mapKorisnikToUser(korisnik: Korisnik): User {
        return User(
            id = korisnik.ID,
            ime = korisnik.Ime,
            prezime = korisnik.Prezime,
            email = korisnik.Email,
            status = korisnik.Status,
            brojMobitela = korisnik.BrojMobitela,
            lozinka = korisnik.Lozinka,
            tipKorisnikaId = korisnik.TipKorisnikaId
        )
    }

    private fun mapTipToType(tip: Tip_korisnika): UserType {
        return UserType(
            id = tip.ID,
            tip = tip.Tip
        )
    }
}