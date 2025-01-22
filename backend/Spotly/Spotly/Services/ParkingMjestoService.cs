using Spotly.Data.Repositories;
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
