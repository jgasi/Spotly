package org.foi.hr.air.spotly.viewmodels

import androidx.lifecycle.*
import com.example.core.vehicle_lookup.network.ResponseListener
import com.example.core.vehicle_lookup.network.models.*
import com.example.ws.network.models.responses.Vehicle
import com.example.ws.request_handlers.GetVehicleByLicensePlateRequestHandler

class VehiclesViewModel: ViewModel() {
    private val _vehicles: MutableLiveData<List<Vehicle>> = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>> = _vehicles

    private val _errorMessage: MutableLiveData<String> = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage


    fun fetchVehicleByLicensePlate(licensePlate: String) {
        val requestHandler = GetVehicleByLicensePlateRequestHandler(licensePlate)
        requestHandler.sendRequest(
            object : ResponseListener<Vehicle> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<Vehicle>) {
                    _vehicles.value = response.data.toList()
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _errorMessage.value = response.message
                }

                override fun onNetworkFailiure(t: Throwable) {
                    _errorMessage.value = "Network error"
                }
            }
        )
    }
}