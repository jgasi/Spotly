using Spotly.Data.Repositories;
using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public class ParkingMjestoService : IParkingMjestoService
    {
        private readonly IParkingMjestoRepository _parkingMjestoRepository;

        public ParkingMjestoService(IParkingMjestoRepository parkingMjestoRepository)
        {
            _parkingMjestoRepository = parkingMjestoRepository;
        }

        public async Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaAsync()
        {
            return await _parkingMjestoRepository.GetAllAsync();
        }

        public async Task UpdateBlockStateAsync(ParkingMjesto parkingMjesto)
        {
            await _parkingMjestoRepository.UpdateAsync(parkingMjesto);
        }

        public async Task<ParkingMjesto> GetParkingMjestoByIdAsync(int id)
        {
            return await _parkingMjestoRepository.GetByIdAsync(id);
        }

        public async Task<int> GetTotalParkingMjestaAsync()
        {
            return await _parkingMjestoRepository.GetCountAsync();
        }

        public async Task<int> GetAvailableParkingMjestaAsync()
        {
            return await _parkingMjestoRepository.GetCountByStatusAsync("Slobodno");
        }

        public async Task<int> GetReservedParkingMjestaAsync()
        {
            return await _parkingMjestoRepository.GetCountByStatusAsync("Rezervirano");
        }

        public async Task<int> GetBlockedParkingMjestaAsync()
        {
            return await _parkingMjestoRepository.GetCountByStatusAsync("Blokirano");
        }
        public async Task<bool> BlokirajParkingMjestoAsync(int id)
        {
            return await _parkingMjestoRepository.BlokirajParkingMjestoAsync(id);
        }
        public async Task<bool> OdblokirajParkingMjestoAsync(int id)
        {
            return await _parkingMjestoRepository.OdblokirajParkingMjestoAsync(id);
        }
        public async Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaIncludeAsync()
        {
            return await _parkingMjestoRepository.GetAllParkingMjestaIncludeAsync();
        }
        public async Task<ParkingMjesto> GetParkingMjestoById(int id)
        {
            return await _parkingMjestoRepository.GetByIdAsync(id);
        }
        public async Task UpdateParkingMjesto(ParkingMjesto parkingMjesto)
        {
            await _parkingMjestoRepository.UpdateAsync(parkingMjesto);
        }
    }
}
