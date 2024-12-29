using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IRezervacijaRepository : IRepository<Rezervacija>
    {
        Task<Rezervacija> GetByVoziloAndParking(int voziloId, int parkingId);
    }
}
