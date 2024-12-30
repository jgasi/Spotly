package org.foi.hr.air.spotly.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.foi.hr.air.spotly.network.QueueService

class QueueViewModel : ViewModel() {
    private val _requests = MutableStateFlow<List<Zahtjev>>(emptyList())
    val requests: StateFlow<List<Zahtjev>> get() = _requests

    init {
        loadQueue()
    }

    fun loadQueue() {
        _requests.value = QueueService.getQueue()
    }

    fun filterQueue(query: String) {
        _requests.value = QueueService.getQueue().filter {
            it.predmet.contains(query, ignoreCase = true)
        }
    }
}
