package org.foi.hr.air.spotly.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import org.foi.hr.air.spotly.data.Zahtjev

object QueueService {
    private val queue = mutableListOf<Zahtjev>()

    fun addToQueue(zahtjev: Zahtjev) {
        queue.add(zahtjev)
    }

    fun getQueue(): List<Zahtjev> {
        return queue
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Funkcija za slanje svih zahtjeva iz queue-a kada ima interneta
    suspend fun processQueue(context: Context) {
        if (isInternetAvailable(context)) {
            while (queue.isNotEmpty()) {
                val zahtjev = queue.first()
                val success = ZahtjevService.addZahtjev(zahtjev)  // Koristi već postojeći servis
                if (success) {
                    queue.removeAt(0)
                    Log.d("QueueService", "Zahtjev uspješno poslan: $zahtjev")
                } else {
                    Log.d("QueueService", "Greška pri slanju zahtjeva: $zahtjev")
                    break
                }
            }
        }
    }
}