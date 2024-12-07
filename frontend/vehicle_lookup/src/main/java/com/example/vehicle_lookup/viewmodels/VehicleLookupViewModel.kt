package com.example.vehicle_lookup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehicle_lookup.data.Vehicle
import com.example.vehicle_lookup.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

public class VehicleLookupViewModel(private val repository: VehicleRepository) : ViewModel() {
    private val _vehicleData = MutableStateFlow<Vehicle?>(null)
    val vehicleData: StateFlow<Vehicle?> = _vehicleData

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun findVehicle(licensePlate: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _vehicleData.value = repository.findVehicle(licensePlate)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun findVehicleByOCR(image: ByteArray) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _vehicleData.value = repository.findVehicleByOCR(image)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}