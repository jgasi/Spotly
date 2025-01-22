using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
	public interface ITipKorisnikaRepository : IRepository<TipKorisnika>
    {
        Task<TipKorisnikaDto> GetByKorisnikIdAsync(int id);
    }
}

