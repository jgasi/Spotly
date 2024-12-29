using Microsoft.EntityFrameworkCore;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class RezervacijaRepository : Repository<Rezervacija>, IRezervacijaRepository
    {
        private readonly SpotlyContext _context;
        public RezervacijaRepository(SpotlyContext context) : base(context)
        {
            _context = context;
        }

        public async Task<Rezervacija> GetByVoziloAndParking(int voziloId, int parkingId)
        {
            return await _context.Rezervacijas.FirstOrDefaultAsync(k => k.VoziloId == voziloId && k.ParkingMjestoId == parkingId);
        }
    }
}
