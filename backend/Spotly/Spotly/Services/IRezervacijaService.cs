using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public interface IRezervacijaService
    {
            Task<IEnumerable<Rezervacija>> GetAllRezervacijeAsync();
            Task<RezervacijaDto> GetRezervacijaByIdAsync(int id);
            Task<RezervacijaDto> GetRezervacijaByVoziloIdAsync(int id);

            Task AddRezervacijaAsync(Rezervacija rezervacija);
            Task UpdateRezervacijaAsync(Rezervacija rezervacija);
            Task DeleteRezervacijaAsync(int id);
            //Task<ParkingMjesto> GetParkingMjestoKorisnikaAsync(int parkingMjestoId);
        Task<ParkingMjesto> GetParkingMjestoKorisnikaAsync(int parkingMjestoId);
        Task<Rezervacija> GetRezervacijaByVoziloAndParkingAsync(int voziloId, int parkingMjestoId);
        Task<Rezervacija> GetLatestRezervacijaByVozilo(int voziloId);
    }
}
