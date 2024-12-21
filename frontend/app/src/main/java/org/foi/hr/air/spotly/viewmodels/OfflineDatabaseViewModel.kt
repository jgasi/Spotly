package org.foi.hr.air.spotly.navigation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OfflineDatabaseViewModel(private val databaseManager: OfflineDatabaseManager) : ViewModel() {
    var backups by mutableStateOf<List<DatabaseBackup>>(emptyList())
        private set

    private var _isDropdownExpanded by mutableStateOf(false)
    val isDropdownExpanded: Boolean get() = _isDropdownExpanded

    init {
        loadBackups()
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _isDropdownExpanded = expanded
    }

    fun setOfflineMode(enabled: Boolean) {
        viewModelScope.launch {
            databaseManager.setOfflineMode(enabled)
        }
    }

    fun applyBackup(backup: DatabaseBackup?) {
        backup?.let {
            viewModelScope.launch {
                databaseManager.restoreBackup(it.timestamp)
            }
        }
    }

    fun syncDatabase() {
        viewModelScope.launch {
            databaseManager.syncWithRemote()
            loadBackups()
        }
    }

    private fun loadBackups() {
        viewModelScope.launch {
            backups = databaseManager.getBackups()
        }
    }
}