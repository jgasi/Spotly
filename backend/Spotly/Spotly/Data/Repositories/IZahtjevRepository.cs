using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IZahtjevRepository : IRepository<Zahtjev>
    {
        Task<IEnumerable<ZahtjevDto>> GetPagedAsync(int page, int pageSize);
        Task<IEnumerable<ZahtjevDto>> GetPagedNaCekanjuAsync(int page, int pageSize);

        Task<ZahtjevDto> GetZahtjevByIdAsync(int id);
        Task<IEnumerable<ZahtjevDto>> GetZahtjevByKorisnikIdAsync(int id);

        Task<ZahtjevDto> UpdateAsync(ZahtjevDto zahtjev);
    }
}
