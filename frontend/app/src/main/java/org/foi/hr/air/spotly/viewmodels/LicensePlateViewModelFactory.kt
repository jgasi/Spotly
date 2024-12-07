package org.foi.hr.air.spotly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehicle_lookup.repository.VehicleRepository
import com.example.vehicle_lookup.viewmodels.VehicleLookupViewModel

class LicensePlateViewModelFactory(
    private val repository: VehicleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleLookupViewModel::class.java)) {
            return VehicleLookupViewModel(repository) as T
        }
        throw IllegalArgumentException("Nepoznata viewmodel klasa")
    }
}
