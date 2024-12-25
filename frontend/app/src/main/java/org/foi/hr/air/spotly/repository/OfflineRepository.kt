package org.foi.hr.air.spotly.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.database.entity.Korisnik
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
            db.korisnikDao().insertAll(korisnici)
        }
    }

    suspend fun getKorisnici(): List<Korisnik> {
        return if (isOfflineMode()) {
            db.korisnikDao().getAll()
        } else {
            api.getAll<Korisnik>()
        }
    }
}