using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class DokumentacijaRepository : Repository<Dokumentacija>, IDokumentacijaRepository
    {
        public DokumentacijaRepository(SpotlyContext context) : base(context) 
        {

        }
    }
}
