using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class TipKorisnikaRepository : Repository<TipKorisnika>, ITipKorisnikaRepository
    {
        private readonly SpotlyContext _context;
        public TipKorisnikaRepository(SpotlyContext context) : base(context)
        {
            _context = context;
        }
    }
}
