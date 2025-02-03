using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IParkingMjestoRepository : IRepository<ParkingMjesto>
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaIncludeAsync();
        Task<int> GetCountAsync();
        Task<int> GetCountByStatusAsync(string status);
        Task<bool> BlokirajParkingMjestoAsync(int id);
        Task<bool> OdblokirajParkingMjestoAsync(int id);
    }
}
