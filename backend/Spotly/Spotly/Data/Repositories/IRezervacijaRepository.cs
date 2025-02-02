using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IRezervacijaRepository : IRepository<Rezervacija>
    {
        Task<RezervacijaDto> GetByIdAsync(int id);
        Task<RezervacijaDto> GetByVoziloIdAsync(int id);
        Task<Rezervacija> GetByVoziloAndParking(int voziloId, int parkingId);
        Task<Rezervacija> GetLatestRezervacijaByVozilo(int voziloId);
    }
}
