using Microsoft.EntityFrameworkCore;
using Spotly.DTOs;
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

        public async Task<int> GetCountAsync()
        {
            return await _context.ParkingMjestos.CountAsync();
        }

        public async Task<int> GetCountByStatusAsync(string status)
        {
            return await _context.ParkingMjestos.CountAsync(p => p.Dostupnost.ToLower() == status.ToLower());
        }
    }
}
