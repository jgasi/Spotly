using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IKorisnikRepository : IRepository<Korisnik>
    {
        Task<Korisnik> GetByEmailAsync(string email);
    }
}
