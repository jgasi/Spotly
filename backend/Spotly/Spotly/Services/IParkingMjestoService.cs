﻿using Spotly.Models;

namespace Spotly.Services
{
    public interface IParkingMjestoService
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaAsync();
        Task<ParkingMjesto> GetParkingMjestoByIdAsync(int id);

        Task<int> GetTotalParkingMjestaAsync();
        Task<int> GetAvailableParkingMjestaAsync();
        Task<int> GetReservedParkingMjestaAsync();
        Task<int> GetBlockedParkingMjestaAsync();
        Task<bool> BlokirajParkingMjestoAsync(int id);
        Task<bool> OdblokirajParkingMjestoAsync(int id);
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaIncludeAsync();
        Task<ParkingMjesto> GetParkingMjestoById(int id);
        Task UpdateParkingMjesto(ParkingMjesto parkingMjesto);
    }
}
