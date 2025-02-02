using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IParkingMjestoRepository : IRepository<ParkingMjesto>
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaIncludeAsync();
    }
}
