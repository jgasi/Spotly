using Spotly.Data.Repositories;
using Spotly.Models;

namespace Spotly.Services
{
    public class RezervacijaService : IRezervacijaService
    {

        private readonly IRezervacijaRepository _rezervacijaRepository;
        private readonly IParkingMjestoRepository _parkingMjestoRepository;
        public RezervacijaService(IRezervacijaRepository rezervacijaRepository, IParkingMjestoRepository parkingMjestoRepository)
        {
            _rezervacijaRepository = rezervacijaRepository;
            _parkingMjestoRepository = parkingMjestoRepository;
        }
        public async Task AddRezervacijaAsync(Rezervacija rezervacija)
        {
            await _rezervacijaRepository.AddAsync(rezervacija);
        }

        public async Task<IEnumerable<Rezervacija>> GetAllRezervacijeAsync()
        {
            return await _rezervacijaRepository.GetAllAsync();
        }

        public Task<ParkingMjesto> GetParkingMjestoKorisnikaAsync(int parkingMjestoId)
        {
            return _parkingMjestoRepository.GetByIdAsync(parkingMjestoId);
        }

        public async Task<Rezervacija> GetRezervacijaByIdAsync(int id)
        {
            return await _rezervacijaRepository.GetByIdAsync(id);
        }

        public async Task UpdateRezervacijaAsync(Rezervacija rezervacija)
        {
            await _rezervacijaRepository.UpdateAsync(rezervacija);
        }
    }
}
