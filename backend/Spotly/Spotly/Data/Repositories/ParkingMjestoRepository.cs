using Spotly.Models;
﻿using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;


namespace Spotly.Data.Repositories
{
    public class ParkingMjestoRepository : Repository<ParkingMjesto>, IParkingMjestoRepository
    {


        private readonly SpotlyContext _context;
        public ParkingMjestoRepository(SpotlyContext context) : base(context)
        {
            _context = context;
        }

        public async Task<int> GetCountAsync()
        {
            return await _context.ParkingMjestos.CountAsync();
        }

        public async Task<int> GetCountByStatusAsync(string status)
        {
            return await _context.ParkingMjestos.CountAsync(p => p.Dostupnost.ToLower() == status.ToLower());
        }
        public async Task<bool> BlokirajParkingMjestoAsync(int id)
        {
            var parkingMjesto = await _context.ParkingMjestos.FindAsync(id);
            if (parkingMjesto == null)
            {
                return false;
            }

            parkingMjesto.Dostupnost = "Blokirano";
            _context.ParkingMjestos.Update(parkingMjesto);
            await _context.SaveChangesAsync();

            return true;
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
        public async Task<bool> OdblokirajParkingMjestoAsync(int id)
        {
            var parkingMjesto = await _context.ParkingMjestos.FindAsync(id);
            if (parkingMjesto == null) return false;

            parkingMjesto.Dostupnost = "Slobodno";
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
