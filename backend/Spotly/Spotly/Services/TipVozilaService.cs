using Spotly.Data.Repositories;
using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
	public class TipVozilaService : ITipVozilaService
	{
        private readonly ITipVozilaRepository _tipVozilaRepository;

        public TipVozilaService(ITipVozilaRepository tipVozilaRepository)
		{
            _tipVozilaRepository = tipVozilaRepository;
		}

        public async Task AddTipVozilaAsync(TipVozila tipVozila)
        {
            await _tipVozilaRepository.AddAsync(tipVozila);
        }

        public async Task DeleteTipVozilaAsync(int id)
        {
            await _tipVozilaRepository.DeleteAsync(id);
        }

        public async Task<IEnumerable<TipVozila>> GetAllTipVozilaAsync()
        {
            return await _tipVozilaRepository.GetAllAsync();
        }

        public async Task<TipVozila> GetTipVozilaByIdAsync(int id)
        {
            return await _tipVozilaRepository.GetByIdAsync(id);

        }

        public async Task UpdateTipVozilaAsync(TipVozila tipVozila)
        {
            await _tipVozilaRepository.UpdateAsync(tipVozila);
        }
    }
}

