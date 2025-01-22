using Spotly.Models;

namespace Spotly.Services
{
	public interface IVoziloService
	{
        Task<IEnumerable<Vozilo>> GetAllVozilaAsync();
        Task<Vozilo> GetVoziloByIdAsync(int id);
        Task<Vozilo> GetVoziloByKorisnikIdAsync(int id);
        Task<Vozilo> GetVoziloByLicensePlateAsync(string licensePlate);
        Task AddVoziloAsync(Vozilo vozilo);
        Task UpdateVoziloAsync(Vozilo vozilo);
        Task DeleteVoziloAsync(int id);
        Task<int> GetTotalVehiclesCountAsync();
    }
}

