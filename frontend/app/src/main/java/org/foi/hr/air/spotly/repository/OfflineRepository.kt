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
    val dokumentacija: List<Dokumentacija>
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
        if (enabled) {
            val korisnici = api.getAll<Korisnik>()
            val tipovi_korisnika = api.getAll<Tip_korisnika>()
            val dokumentacija = api.getAll<Dokumentacija>()

            db.korisnikDao().apply {
                deleteAll()
                insertAll(korisnici)
            }
            db.tipKorisnikaDao().apply {
                deleteAll()
                insertAll(tipovi_korisnika)
            }
            db.dokumentacijaDao().apply {
                deleteAll()
                insertAll(dokumentacija)
            }
        }
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
        }
    }

    suspend fun getAll(): AllData {
        return if (isOfflineMode()) {
            val korisnici = db.korisnikDao().getAll()
            val tipoviKorisnika = db.tipKorisnikaDao().getAll()
            val dokumentacija = db.dokumentacijaDao().getAll()
            AllData(korisnici, tipoviKorisnika, dokumentacija)
        } else {
            val korisnici = api.getAll<Korisnik>()
            val tipoviKorisnika = api.getAll<Tip_korisnika>()
            val dokumentacija = api.getAll<Dokumentacija>()
            AllData(korisnici, tipoviKorisnika, dokumentacija)
        }
    }

    suspend fun getKorisnici(): List<Korisnik> {
        return if (isOfflineMode()) {
            db.korisnikDao().getAll()
        } else {
            api.getAll<Korisnik>()
        }
    }

    suspend fun getTipKorisnika(): List<Tip_korisnika> {
        return if (isOfflineMode()) {
            db.tipKorisnikaDao().getAll()
        } else {
            api.getAll<Tip_korisnika>()
        }
    }

    suspend fun getDokumentacija(): List<Dokumentacija> {
        return if (isOfflineMode()) {
            db.dokumentacijaDao().getAll()
        } else {
            api.getAll<Dokumentacija>()
        }
    }
}