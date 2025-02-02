using Microsoft.EntityFrameworkCore;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class ParkingMjestoRepository : Repository<ParkingMjesto>, IParkingMjestoRepository
    {

        private readonly SpotlyContext _context;
        public ParkingMjestoRepository(SpotlyContext context) : base(context)
        {

            _context = context;
        }

        public async Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaIncludeAsync()
        {
            //return await _context.ParkingMjestos.Include(p => p.Rezervacijas).ToListAsync();

            return await _context.ParkingMjestos
                .Select(p => new ParkingMjesto
                {
                    Id = p.Id,
                    Status = p.Status,
                    Dostupnost = p.Dostupnost,
                    TipMjestaId = p.TipMjestaId,
                    TipMjesta = p.TipMjesta,
                    Rezervacijas = p.Rezervacijas
                        .OrderByDescending(r => r.DatumVrijemeRezervacije)
                        .Take(1)
                        .ToList()
                })
                .ToListAsync();
        }
    }
}
