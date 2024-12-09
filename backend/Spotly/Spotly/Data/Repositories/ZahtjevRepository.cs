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

        public async Task<ZahtjevDto> GetZahtjevByIdAsync(int id)
        {
            var zahtjev = await _context.Zahtjevs
                .Where(z => z.Id == id)
                .Select(z => new ZahtjevDto
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
                })
                .FirstOrDefaultAsync();

            return zahtjev;
        }

        public async Task<IEnumerable<ZahtjevDto>> GetZahtjevByKorisnikIdAsync(int id)
        {
            var zahtjev = await _context.Zahtjevs
                .Where(z => z.KorisnikId == id)
                .Select(z => new ZahtjevDto
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
                }).ToListAsync();

            return zahtjev;
        }

        public async Task<ZahtjevDto> UpdateAsync(ZahtjevDto zahtjev)
        {
            if (zahtjev == null)
            {
                throw new ArgumentNullException(nameof(zahtjev), "Podaci za zahtjev ne smiju biti null.");
            }

            // Pronađi postojeći zahtjev u bazi podataka prema ID-u
            var existingZahtjev = await _context.Zahtjevs.FindAsync(zahtjev.Id);
            if (existingZahtjev == null)
            {
                throw new KeyNotFoundException($"Zahtjev s ID-om {zahtjev.Id} nije pronađen.");
            }

            existingZahtjev.Odgovor = zahtjev.Odgovor;
            existingZahtjev.Status = zahtjev.Status;

            _context.Zahtjevs.Update(existingZahtjev);
            await _context.SaveChangesAsync();

            var updatedDto = new ZahtjevDto
            {
                Id = existingZahtjev.Id,
                Odgovor = existingZahtjev.Odgovor,
                Status = existingZahtjev.Status
            };

            return updatedDto;
        }

    }
}
