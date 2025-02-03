using Spotly.Data.Repositories;
using Spotly.DTOs;
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

        public async Task DeleteRezervacijaAsync(int id)
        {
            await _rezervacijaRepository.DeleteAsync(id);
        }

        public async Task<IEnumerable<Rezervacija>> GetAllRezervacijeAsync()
        {
            return await _rezervacijaRepository.GetAllAsync();
        }

        public Task<ParkingMjesto> GetParkingMjestoKorisnikaAsync(int parkingMjestoId)
        {
            return _parkingMjestoRepository.GetByIdAsync(parkingMjestoId);
        }

        public async Task<RezervacijaDto> GetRezervacijaByIdAsync(int id)
        {
            return await _rezervacijaRepository.GetByIdAsync(id);
        }

        public async Task<RezervacijaDto> GetRezervacijaByVoziloIdAsync(int id)
        {
            return await _rezervacijaRepository.GetByVoziloIdAsync(id);
        }

        public async Task<Rezervacija> GetRezervacijaByVoziloAndParkingAsync(int voziloId, int parkingMjestoId)
        {
            return await _rezervacijaRepository.GetByVoziloAndParking(voziloId, parkingMjestoId);
        }

        public async Task UpdateRezervacijaAsync(Rezervacija rezervacija)
        {
            await _rezervacijaRepository.UpdateAsync(rezervacija);
        }

        public async Task<Rezervacija> GetLatestRezervacijaByVozilo(int voziloId)
        {
            return await _rezervacijaRepository.GetLatestRezervacijaByVozilo(voziloId);
        }
    }
}
