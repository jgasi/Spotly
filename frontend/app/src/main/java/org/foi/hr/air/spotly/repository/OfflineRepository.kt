package org.foi.hr.air.spotly.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.entity.*
import org.foi.hr.air.spotly.datastore.OfflinePreferences.IS_OFFLINE
import org.foi.hr.air.spotly.datastore.OfflinePreferences.dataStore
import org.foi.hr.air.spotly.network.ApiService
import java.io.IOException

data class AllData(
    val korisnici: List<Korisnik>,
    val tipovi_korisnika: List<Tip_korisnika>,
    val dokumentacija: List<Dokumentacija>,
    val vozila: List<Vozilo>,
    val tipovi_vozila: List<Tip_vozila>,
    val zahtjevi: List<Zahtjev>,
    val kazne: List<Kazna>
)

class OfflineRepository(
    private val api: ApiService,
    private val db: AppDatabase,
    private val context: Context
) {
    private val dataStore = context.dataStore

    fun ReturnEmptyData(): AllData {
        return AllData(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    }

    suspend fun isOfflineMode(): Boolean {
        return context.dataStore.data.first()[IS_OFFLINE] ?: false
    }

    suspend fun setOfflineMode(enabled: Boolean) {
        context.dataStore.edit { it[IS_OFFLINE] = enabled }
    }

    suspend fun isDbEmpty(): Boolean {
        return db.korisnikDao().getAll().isEmpty() &&
                db.tipKorisnikaDao().getAll().isEmpty() &&
                db.dokumentacijaDao().getAll().isEmpty() &&
                db.zahtjevDao().getAll().isEmpty() &&
                db.kaznaDao().getAll().isEmpty() &&
                db.tipVozilaDao().getAll().isEmpty() &&
                db.voziloDao().getAll().isEmpty() &&
                db.kaznaDao().getAll().isEmpty() &&
                db.zahtjevDao().getAll().isEmpty()
    }

    suspend fun deleteAll(){
        if (!isOfflineMode()) {
            db.korisnikDao().deleteAll()
            db.tipKorisnikaDao().deleteAll()
            db.dokumentacijaDao().deleteAll()
            db.voziloDao().deleteAll()
            db.tipVozilaDao().deleteAll()
            db.zahtjevDao().deleteAll()
            db.kaznaDao().deleteAll()
        }
    }

    suspend fun getAll(): AllData {
        return if (!isOfflineMode()) {
            val korisnici = db.korisnikDao().getAll()
            val tipoviKorisnika = db.tipKorisnikaDao().getAll()
            val dokumentacija = db.dokumentacijaDao().getAll()
            val vozilo = db.voziloDao().getAll()
            val tipoviVozila = db.tipVozilaDao().getAll()
            val zahtjevi = db.zahtjevDao().getAll()
            val kazne = db.kaznaDao().getAll()

            AllData(korisnici, tipoviKorisnika, dokumentacija, vozilo, tipoviVozila, zahtjevi, kazne)
        } else {
            try {
                val korisnici = api.getAll<Korisnik>()
                val tipoviKorisnika = api.getAll<Tip_korisnika>()
                val dokumentacija = api.getAll<Dokumentacija>()
                val vozilo = api.getAll<Vozilo>()
                val tipoviVozila = api.getAll<Tip_vozila>()
                val zahtjevi = api.getAll<Zahtjev>()
                val kazne = api.getAll<Kazna>()

                return AllData(korisnici, tipoviKorisnika, dokumentacija, vozilo, tipoviVozila, zahtjevi, kazne)
            } catch (ex: IOException) {
                return ReturnEmptyData()
            }
        }
    }
}