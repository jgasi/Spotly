using Microsoft.EntityFrameworkCore;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class ParkingMjestoRepository : Repository<ParkingMjesto>, IParkingMjestoRepository
    {
        public ParkingMjestoRepository(SpotlyContext context) : base(context)
        {

        }
    }
}
