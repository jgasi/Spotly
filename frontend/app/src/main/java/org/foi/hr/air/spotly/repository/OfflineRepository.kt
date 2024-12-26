package org.foi.hr.air.spotly.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.entity.Korisnik
import org.foi.hr.air.spotly.database.entity.Tip_korisnika
import org.foi.hr.air.spotly.datastore.OfflinePreferences.IS_OFFLINE
import org.foi.hr.air.spotly.datastore.OfflinePreferences.dataStore
import org.foi.hr.air.spotly.network.ApiService

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
            db.korisnikDao().apply {
                deleteAll()
                insertAll(korisnici)
            }
            db.tipKorisnikaDao().apply {
                deleteAll()
                insertAll(tipovi_korisnika)
            }
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
}