using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class ZahtjevRepository : Repository<Zahtjev>, IZahtjevRepository
    {
        public ZahtjevRepository(SpotlyContext context) : base(context)
        {

        }

        public async Task<IEnumerable<ZahtjevDto>> GetPagedAsync(int pageNumber, int pageSize)
        {
            var zahtjevi = await _context.Zahtjevs
                .Skip((pageNumber - 1) * pageSize)
                .Take(pageSize)
                .ToListAsync();

            var zahtjeviDto = zahtjevi.Select(z => new ZahtjevDto
            {
                Id = z.Id,
                Predmet = z.Predmet,
                Poruka = z.Poruka,
                Odgovor = z.Odgovor,
                Status = z.Status,
                DatumVrijeme = z.DatumVrijeme.ToString(),
                AdminId = z.AdminId,
                KorisnikId = z.KorisnikId,
                TipZahtjevaId = z.TipZahtjevaId
            });

            return zahtjeviDto;
        }
    }
}
