using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public interface IParkingMjestoService
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaAsync();
        Task<ParkingMjesto> GetParkingMjestoByIdAsync(int id);

        Task<int> GetTotalParkingMjestaAsync();
        Task<int> GetAvailableParkingMjestaAsync();
        Task<int> GetReservedParkingMjestaAsync();
        Task<int> GetBlockedParkingMjestaAsync();
    }
}
