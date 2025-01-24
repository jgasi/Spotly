using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public interface IZahtjevService
    {
        Task<IEnumerable<Zahtjev>> GetAllZahtjeviAsync();
        Task<IEnumerable<ZahtjevDto>> GetAllZahtjeviNaCekanjuAsync();
        Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviAsync(int pageNumber, int pageSize);
        Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviNaCekanjuAsync(int pageNumber, int pageSize);
        Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviOdgovoreniAsync(int pageNumber, int pageSize);

        Task<ZahtjevDto> GetZahtjevByIdAsync(int id);
        Task<IEnumerable<ZahtjevDto>> GetZahtjevByKorisnikIdAsync(int id);

        Task AddZahtjevAsync(Zahtjev zahtjev);
        Task UpdateZahtjevAsync(ZahtjevDto zahtjev);
        Task DeleteZahtjevAsync(int id);
    }
}
