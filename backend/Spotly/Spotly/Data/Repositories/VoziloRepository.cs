using Microsoft.EntityFrameworkCore;
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

        public async Task<Vozilo> GetByLicensePlateAsync(string licensePlate)
        {
            if (string.IsNullOrEmpty(licensePlate))
                return null;

            return await _context.Vozilos
                .Include(v => v.Korisnik)
                .Include(v => v.TipVozila)
                .FirstOrDefaultAsync(v => v.Registracija.ToLower() == licensePlate.ToLower());
        }

        public async Task<Vozilo> GetVoziloByKorisnikIdAsync(int id)
        {
            return await _context.Vozilos
                .Include(v => v.Korisnik)
                .Include(v => v.TipVozila)
                .FirstOrDefaultAsync(v => v.KorisnikId == id);
        }

        public async Task<int> GetTotalVehiclesCountAsync()
        {
            return await _context.Vozilos.CountAsync();
        }
    }
}

