using Spotly.Models;

namespace Spotly.Services
{
    public interface IParkingMjestoService
    {
        Task<IEnumerable<ParkingMjesto>> GetAllParkingMjestaAsync();
        Task UpdateBlockStateAsync(ParkingMjesto parkingMjesto);
    }
}
