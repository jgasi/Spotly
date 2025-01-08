package org.foi.hr.air.spotly.network

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.foi.hr.air.spotly.data.Zahtjev

object QueueService {
    private const val QUEUE_PREF = "queue_pref"
    private const val KEY_QUEUE = "key_queue"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appContext: Context

    private val queue = mutableListOf<Zahtjev>()

    fun init(context: Context) {
        appContext = context.applicationContext
        sharedPreferences = appContext.getSharedPreferences(QUEUE_PREF, Context.MODE_PRIVATE)
        loadQueue()

        NetworkMonitor(appContext).startMonitoring()
    }

    private fun loadQueue() {
        val jsonQueue = sharedPreferences.getString(KEY_QUEUE, "[]") ?: "[]"
        queue.clear()
        try {
            queue.addAll(Json.decodeFromString(ListSerializer(Zahtjev.serializer()), jsonQueue))
        } catch (e: Exception) {
            Log.e("QueueService", "Greška pri učitavanju queue-a: ${e.message}")
        }
    }

    private fun saveQueue() {
        val jsonQueue = Json.encodeToString(ListSerializer(Zahtjev.serializer()), queue)
        sharedPreferences.edit().putString(KEY_QUEUE, jsonQueue).apply()
    }

    fun addToQueue(zahtjev: Zahtjev) {
        queue.add(zahtjev)
        saveQueue()
        Log.d("QueueService", "Zahtjev dodan u queue: $zahtjev")
    }

    fun getQueue(): List<Zahtjev> = queue

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun processQueue() {
        if (isInternetAvailable()) {
            val iterator = queue.iterator()
            while (iterator.hasNext()) {
                val zahtjev = iterator.next()
                val success = ZahtjevService.addZahtjev(zahtjev)
                if (success) {
                    iterator.remove()
                    saveQueue()
                    Log.d("QueueService", "Zahtjev uspješno poslan i uklonjen: $zahtjev")
                } else {
                    Log.d("QueueService", "Greška prilikom slanja zahtjeva: $zahtjev")
                    break
                }
            }
        } else {
            Log.d("QueueService", "Internet nije dostupan, zahtjevi ostaju u redu.")
        }
    }

    fun deleteFromQueue(zahtjevId: Int) {
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            val zahtjev = iterator.next()
            if (zahtjev.id == zahtjevId) {
                iterator.remove()  // Brisanje zahtjeva iz reda
                saveQueue()  // Ažuriranje podataka u SharedPreferences
                Log.d("QueueService", "Zahtjev s ID $zahtjevId uspješno obrisan.")
                return
            }
        }
        Log.d("QueueService", "Zahtjev s ID $zahtjevId nije pronađen u redu.")
    }
}

class NetworkMonitor(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun startMonitoring() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("NetworkMonitor", "Internet dostupan - procesiranje reda.")

                // Kada internet postane dostupan, pokrećemo `processQueue` iz coroutine
                GlobalScope.launch(Dispatchers.Main) {
                    QueueService.processQueue()  // Pozivamo suspend funkciju
                }
            }
        })
    }
}