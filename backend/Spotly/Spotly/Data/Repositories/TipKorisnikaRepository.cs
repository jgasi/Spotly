using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;
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

        public async Task<TipKorisnikaDto> GetByKorisnikIdAsync(int id)
        {
            var userType = await _context.TipKorisnikas
                .Where(ut => ut.Korisniks.Any(k => k.Id == id))
                .Select(ut => new TipKorisnikaDto
                {
                    Id = ut.Id,
                    Tip = ut.Tip
                })
                .FirstOrDefaultAsync();

            if (userType == null)
            {
                throw new KeyNotFoundException($"Tip korisnika za korisnika s ID-om {id} nije pronađen.");
            }

            return userType;
        }
    }
}
