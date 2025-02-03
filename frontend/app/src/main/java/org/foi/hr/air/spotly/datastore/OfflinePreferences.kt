package org.foi.hr.air.spotly.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.util.prefs.Preferences

object OfflinePreferences {
    val Context.dataStore by preferencesDataStore("settings")
    val IS_OFFLINE = booleanPreferencesKey("is_offline")
}