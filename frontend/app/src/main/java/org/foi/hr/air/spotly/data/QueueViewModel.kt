package org.foi.hr.air.spotly.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.foi.hr.air.spotly.network.QueueService

class QueueViewModel : ViewModel() {
    var requests: MutableState<List<Zahtjev>> = mutableStateOf(emptyList())

    init {
        loadQueue()
    }

    fun loadQueue() {
        requests.value = QueueService.getQueue()
    }
}