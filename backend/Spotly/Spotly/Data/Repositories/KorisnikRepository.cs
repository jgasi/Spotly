using Microsoft.EntityFrameworkCore;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class KorisnikRepository : Repository<Korisnik>, IKorisnikRepository
    {
        private readonly SpotlyContext _context;
        public KorisnikRepository(SpotlyContext context) : base(context) 
        {
            _context = context;
        }

        public async Task<Korisnik> GetByEmailAsync(string email)
        {
            if (string.IsNullOrEmpty(email))
                return null;

            return await _context.Korisniks.FirstOrDefaultAsync(k => k.Email == email);
        }
    }
}
