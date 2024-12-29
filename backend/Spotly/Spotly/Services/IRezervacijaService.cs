using Spotly.Models;

namespace Spotly.Services
{
    public interface IRezervacijaService
    {
        Task<IEnumerable<Rezervacija>> GetAllRezervacijeAsync();
        Task<Rezervacija> GetRezervacijaByIdAsync(int id);
        Task AddRezervacijaAsync(Rezervacija rezervacija);
        Task UpdateRezervacijaAsync(Rezervacija rezervacija);
        Task<ParkingMjesto> GetParkingMjestoKorisnikaAsync(int parkingMjestoId);
        Task<Rezervacija> GetRezervacijaByVoziloAndParkingAsync(int voziloId, int parkingMjestoId);
    }
}
