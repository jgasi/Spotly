using System;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
	public class VoziloRepository : Repository<Vozilo>, IVoziloRepository
	{
        private readonly SpotlyContext _context;
        public VoziloRepository(SpotlyContext context) : base(context)
        {
            _context = context;
        }
    }
}

