package org.foi.hr.air.spotly.network

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.foi.hr.air.spotly.data.Zahtjev

object QueueService {
    private const val QUEUE_PREF = "queue_pref"
    private const val KEY_QUEUE = "key_queue"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appContext: Context

    private val queue = mutableListOf<Zahtjev>()

    fun initialize(context: Context) {
        appContext = context.applicationContext
        sharedPreferences = appContext.getSharedPreferences(QUEUE_PREF, Context.MODE_PRIVATE)
        loadQueue()
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

    fun hasInternet(): Boolean = isInternetAvailable()
}