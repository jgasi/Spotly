﻿using Spotly.Models;

namespace Spotly.Services
{
    public interface IParkingMjestoService
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaAsync();
        Task<ParkingMjesto> GetParkingMjestoById(int id);
        Task UpdateParkingMjesto(ParkingMjesto parkingMjesto);
    }
}
