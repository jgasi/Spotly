using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Data.Repositories
{
    public interface IParkingMjestoRepository : IRepository<ParkingMjesto>
    {
        Task<int> GetCountAsync();
        Task<int> GetCountByStatusAsync(string status);
    }
}
