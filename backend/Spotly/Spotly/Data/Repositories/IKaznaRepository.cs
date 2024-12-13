using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IKaznaRepository : IRepository<Kazna>
    {
        Task<IEnumerable<KaznaDto>> GetByKorisnikId(int userId);
    }
}
