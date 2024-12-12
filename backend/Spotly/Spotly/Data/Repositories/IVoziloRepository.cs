using Spotly.Models;

namespace Spotly.Data.Repositories
{
	public interface IVoziloRepository : IRepository<Vozilo>
    {
        Task<Vozilo> GetByLicensePlateAsync(string licensePlate);
        Task<Vozilo> GetVoziloByKorisnikIdAsync(int id);
    }
}

