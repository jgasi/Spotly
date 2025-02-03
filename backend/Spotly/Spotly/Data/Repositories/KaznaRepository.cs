using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class KaznaRepository : Repository<Kazna>, IKaznaRepository
    {
        public KaznaRepository(SpotlyContext context) : base(context)
        {

        }

        public async Task<IEnumerable<KaznaDto>> GetByKorisnikId(int userId)
        {
            return await _context.Kaznas
                .Where(kazna => kazna.KorisnikId == userId)
                .Select(kazna => new KaznaDto
                {
                    Id = kazna.Id,
                    Razlog = kazna.Razlog,
                    NovcaniIznos = kazna.NovcaniIznos,
                    DatumVrijeme = kazna.DatumVrijeme.ToString("yyyy-MM-dd'T'HH:mm:ss"),
                    AdminId = kazna.AdminId,
                    KorisnikId = kazna.KorisnikId,
                    TipKazneId = kazna.TipKazneId
                })
                .ToListAsync();
        }

        public async Task<int> GetTotalKazneCountAsync()
        {
            return await _context.Kaznas.CountAsync();
        }

        public async Task<int> GetKazneCountByUserIdAsync(int userId)
        {
            return await _context.Kaznas.CountAsync(k => k.KorisnikId == userId);
        }
    }
}
