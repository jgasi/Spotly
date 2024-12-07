using Spotly.Data.Repositories;
using Spotly.Models;

namespace Spotly.Services
{
	public class VoziloService : IVoziloService
	{
        private readonly IVoziloRepository _voziloRepository;

        public VoziloService(IVoziloRepository voziloRepository)
		{
            _voziloRepository = voziloRepository;
		}

        public async Task AddVoziloAsync(Vozilo vozilo)
        {
            await _voziloRepository.AddAsync(vozilo);
        }

        public async Task DeleteVoziloAsync(int id)
        {
            await _voziloRepository.DeleteAsync(id);
        }

        public async Task<IEnumerable<Vozilo>> GetAllVozilaAsync()
        {
            return await _voziloRepository.GetAllAsync();
        }

        public async Task<Vozilo> GetVoziloByIdAsync(int id)
        {
            return await _voziloRepository.GetByIdAsync(id);
        }

        public async Task<Vozilo> GetVoziloByLicensePlateAsync(string licensePlate)
        {
            return await _voziloRepository.GetByLicensePlateAsync(licensePlate);
        }

        public async Task UpdateVoziloAsync(Vozilo vozilo)
        {
            await _voziloRepository.UpdateAsync(vozilo);
        }
    }
}

