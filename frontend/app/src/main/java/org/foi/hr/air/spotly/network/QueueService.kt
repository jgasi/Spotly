package org.foi.hr.air.spotly.network

import org.foi.hr.air.spotly.data.Zahtjev

object QueueService {
    private val queue = mutableListOf<Zahtjev>()

    fun getQueue(): List<Zahtjev> {
        return queue
    }

    fun addToQueue(zahtjev: Zahtjev) {
        queue.add(zahtjev)
    }

    fun clearQueue() {
        queue.clear()
    }
}