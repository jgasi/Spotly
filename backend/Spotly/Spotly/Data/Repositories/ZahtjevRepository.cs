using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class ZahtjevRepository : Repository<Zahtjev>, IZahtjevRepository
    {
        public ZahtjevRepository(SpotlyContext context) : base(context)
        {

        }
    }
}
