using Spotly.Models;

namespace Spotly.Services
{
	public interface IVoziloService
	{
        Task<IEnumerable<Vozilo>> GetAllVozilaAsync();
        Task<Vozilo> GetVoziloByIdAsync(int id);
        Task AddVoziloAsync(Vozilo vozilo);
        Task UpdateVoziloAsync(Vozilo vozilo);
        Task DeleteVoziloAsync(int id);
    }
}

