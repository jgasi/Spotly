using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class KorisnikRepository : Repository<Korisnik>, IKorisnikRepository
    {
        public KorisnikRepository(SpotlyContext context) : base(context) 
        { 

        }
    }
}
