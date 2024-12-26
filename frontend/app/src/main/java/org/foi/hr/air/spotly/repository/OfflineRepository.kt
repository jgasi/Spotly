package org.foi.hr.air.spotly.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.entity.*
import org.foi.hr.air.spotly.datastore.OfflinePreferences.IS_OFFLINE
import org.foi.hr.air.spotly.datastore.OfflinePreferences.dataStore
import org.foi.hr.air.spotly.network.ApiService

data class AllData(
    val korisnici: List<Korisnik>,
    val tipovi_korisnika: List<Tip_korisnika>,
    val dokumentacija: List<Dokumentacija>,
    val vozila: List<Vozilo>,
    val tipovi_vozila: List<Tip_vozila>
)

class OfflineRepository(
    private val api: ApiService,
    private val db: AppDatabase,
    private val context: Context
) {
    private val dataStore = context.dataStore

    suspend fun isOfflineMode(): Boolean {
        return context.dataStore.data.first()[IS_OFFLINE] ?: false
    }

    suspend fun setOfflineMode(enabled: Boolean) {
        context.dataStore.edit { it[IS_OFFLINE] = enabled }
    }

    suspend fun isDbEmpty(): Boolean {
        val korisniciEmpty = db.korisnikDao().getAll().isEmpty()
        val tipovi = db.tipKorisnikaDao().getAll().isEmpty()
        val dok = db.dokumentacijaDao().getAll().isEmpty()
        return korisniciEmpty && tipovi && dok
    }

    suspend fun deleteAll(){
        if (!isOfflineMode()) {
            db.korisnikDao().deleteAll()
            db.tipKorisnikaDao().deleteAll()
            db.dokumentacijaDao().deleteAll()
            db.voziloDao().deleteAll()
            db.tipVozilaDao().deleteAll()
        }
    }

    suspend fun getAll(): AllData {
        return if (!isOfflineMode()) {
            val korisnici = db.korisnikDao().getAll()
            val tipoviKorisnika = db.tipKorisnikaDao().getAll()
            val dokumentacija = db.dokumentacijaDao().getAll()
            val vozilo = db.voziloDao().getAll()
            val tipoviVozila = db.tipVozilaDao().getAll()

            AllData(korisnici, tipoviKorisnika, dokumentacija, vozilo, tipoviVozila)
        } else {
            val korisnici = api.getAll<Korisnik>()
            val tipoviKorisnika = api.getAll<Tip_korisnika>()
            val dokumentacija = api.getAll<Dokumentacija>()
            val vozilo = api.getAll<Vozilo>()
            val tipoviVozila = api.getAll<Tip_vozila>()

            AllData(korisnici, tipoviKorisnika, dokumentacija, vozilo, tipoviVozila)
        }
    }
}