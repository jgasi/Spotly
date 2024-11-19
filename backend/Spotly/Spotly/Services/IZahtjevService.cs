using Spotly.Models;

namespace Spotly.Services
{
    public interface IZahtjevService
    {
        Task<IEnumerable<Zahtjev>> GetAllZahtjeviAsync();
        Task<Zahtjev> GetZahtjevByIdAsync(int id);
        Task AddZahtjevAsync(Zahtjev zahtjev);
        Task UpdateZahtjevAsync(Zahtjev zahtjev);
        Task DeleteZahtjevAsync(int id);
    }
}
