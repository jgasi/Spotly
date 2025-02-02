using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;
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
        public async Task<RezervacijaDto> GetByIdAsync(int id)
        {
            var rezervacija = await _context.Rezervacijas.FindAsync(id);

            if (rezervacija == null)
            {
                return null;
            }

            return new RezervacijaDto
            {
                Id = rezervacija.Id,
                Datum_vrijeme_rezervacije = rezervacija.DatumVrijemeRezervacije,
                Datum_vrijeme_odlaska = rezervacija.DatumVrijemeOdlaska ?? DateTime.MinValue,
                VoziloId = rezervacija.VoziloId,
                Parking_mjestoId = rezervacija.ParkingMjestoId
            };
        }

        public async Task<RezervacijaDto> GetByVoziloIdAsync(int id)
        {
            var rezervacija = await _context.Rezervacijas.Where(v => v.VoziloId == id).SingleOrDefaultAsync();

            if (rezervacija == null)
            {
                return null;
            }

            return new RezervacijaDto
            {
                Id = rezervacija.Id,
                Datum_vrijeme_rezervacije = rezervacija.DatumVrijemeRezervacije,
                Datum_vrijeme_odlaska = rezervacija.DatumVrijemeOdlaska ?? DateTime.MinValue,
                VoziloId = rezervacija.VoziloId,
                Parking_mjestoId = rezervacija.ParkingMjestoId
            };
        }
    }
}
