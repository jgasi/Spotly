using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IZahtjevRepository : IRepository<Zahtjev>
    {
        Task<IEnumerable<ZahtjevDto>> GetPagedAsync(int page, int pageSize);
    }
}
