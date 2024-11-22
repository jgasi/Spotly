using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class KorisnikRepository : Repository<Korisnik>, IKorisnikRepository
    {
        public KorisnikRepository(SpotlyContext context) : base(context) 
        { 

        }

        public async Task<Korisnik> GetByEmailAsync(string email)
        {
            return await _dbSet.FirstOrDefaultAsync(k => k.Email == email);
        }
    }
}
