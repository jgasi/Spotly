using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class TipVozilaRepository : Repository<TipVozila>, ITipVozilaRepository
    {
        private readonly SpotlyContext _context;
        public TipVozilaRepository(SpotlyContext context) : base(context)
        {
            _context = context;
        }
    }
}

