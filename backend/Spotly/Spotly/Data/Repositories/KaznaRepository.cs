using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public class KaznaRepository : Repository<Kazna>, IKaznaRepository
    {
        public KaznaRepository(SpotlyContext context) : base(context)
        {

        }
    }
}
