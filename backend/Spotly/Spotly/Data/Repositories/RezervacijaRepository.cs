using Azure.Core;
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

            var timeNow = DateTime.Now;
            return await _context.Rezervacijas.FirstOrDefaultAsync(k => k.VoziloId == voziloId && k.ParkingMjestoId == parkingId && k.DatumVrijemeOdlaska > timeNow);
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

        public async Task<Rezervacija> GetLatestRezervacijaByVozilo(int voziloId)
        {
            //var timeNow = DateTime.Now;
            //var conflictingReservation = await _context.Rezervacijas.Where(r => r.VoziloId == voziloId && r.DatumVrijemeOdlaska > DateTime.Now).FirstOrDefaultAsync();

            var timeNow = DateTime.Now;
            var conflictingReservation = await _context.Rezervacijas
                .Where(r => r.VoziloId == voziloId && r.DatumVrijemeOdlaska > timeNow)
                .FirstOrDefaultAsync();



            return conflictingReservation;
        }
    }
}
