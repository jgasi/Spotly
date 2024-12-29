package org.foi.hr.air.spotly.repository

import android.content.Context
import android.net.Network
import androidx.datastore.preferences.core.edit
import com.example.core.vehicle_lookup.network.NetworkManager
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
    private val networkManager = NetworkManager(context)

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
        return if (networkManager.isNetworkAvailable()) {
            try {
                val korisnici = api.getAll<Korisnik>()
                val tipoviKorisnika = api.getAll<Tip_korisnika>()
                val dokumentacija = api.getAll<Dokumentacija>()
                val vozila = api.getAll<Vozilo>()
                val tipoviVozila = api.getAll<Tip_vozila>()
                val zahtjevi = api.getAll<Zahtjev>()
                val kazne = api.getAll<Kazna>()

                db.korisnikDao().insertAll(korisnici)
                db.tipKorisnikaDao().insertAll(tipoviKorisnika)
                db.dokumentacijaDao().insertAll(dokumentacija)
                db.voziloDao().insertAll(vozila)
                db.tipVozilaDao().insertAll(tipoviVozila)
                db.zahtjevDao().insertAll(zahtjevi)
                db.kaznaDao().insertAll(kazne)

                AllData(korisnici, tipoviKorisnika, dokumentacija, vozila,
                    tipoviVozila, zahtjevi, kazne)
            } catch (e: IOException) {
                getFromLocalDatabase()
            }
        } else {
            getFromLocalDatabase()
        }
    }

    private suspend fun getFromLocalDatabase(): AllData {
        return AllData(
            korisnici = db.korisnikDao().getAll(),
            tipovi_korisnika = db.tipKorisnikaDao().getAll(),
            dokumentacija = db.dokumentacijaDao().getAll(),
            vozila = db.voziloDao().getAll(),
            tipovi_vozila = db.tipVozilaDao().getAll(),
            zahtjevi = db.zahtjevDao().getAll(),
            kazne = db.kaznaDao().getAll()
        )
    }
}