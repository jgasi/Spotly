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

        public async Task<ParkingMjesto> GetParkingMjestoByIdAsync(int id)
        {
            return await _parkingMjestoRepository.GetByIdAsync(id);
        }
    }
}
