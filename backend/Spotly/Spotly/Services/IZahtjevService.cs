using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public interface IZahtjevService
    {
        Task<IEnumerable<Zahtjev>> GetAllZahtjeviAsync();
        Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviAsync(int pageNumber, int pageSize);
        Task<Zahtjev> GetZahtjevByIdAsync(int id);
        Task AddZahtjevAsync(Zahtjev zahtjev);
        Task UpdateZahtjevAsync(Zahtjev zahtjev);
        Task DeleteZahtjevAsync(int id);
    }
}
