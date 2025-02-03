package org.foi.hr.air.spotly.datastore

import com.example.core.vehicle_lookup.UserData
import com.example.core.vehicle_lookup.VehicleData
import com.example.core.vehicle_lookup.VehicleLookupDataSource
import com.example.core.vehicle_lookup.VehicleTypeData
import org.foi.hr.air.spotly.database.AppDatabase

class RoomVehicleLookupDataSource(private val db: AppDatabase): VehicleLookupDataSource {
    override suspend fun getVehicleByLicensePlate(licensePlate: String): VehicleData? {
        val vehicle = db.voziloDao().getByLicensePlate(licensePlate).firstOrNull() ?: return null
        val user = db.korisnikDao().getById(vehicle.KorisnikID)
        val vehicleType = db.tipVozilaDao().getById(vehicle.TipVozilaId)

        return VehicleData(
            id = vehicle.ID,
            marka = vehicle.Marka,
            model = vehicle.Model,
            godiste = vehicle.GodinaProizvodnje,
            registracija = vehicle.RegistracijskaOznaka,
            status = "Aktivno",
            tipVozilaId = vehicle.TipVozilaId,
            korisnikId = vehicle.KorisnikID,
            korisnik = user?.let {
                UserData(
                    id = it.ID,
                    ime = it.Ime,
                    prezime = it.Prezime,
                    email = it.Email,
                    brojMobitela = it.BrojMobitela,
                    status = it.Status
                )
            },
            tipVozila = vehicleType?.let {
                VehicleTypeData(
                    id = it.ID,
                    tip = it.Tip
                )
            }
        )
    }
}