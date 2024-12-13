using Spotly.Models;

namespace Spotly.Services
{
	public interface ITipVozilaService
	{
        Task<IEnumerable<TipVozila>> GetAllTipVozilaAsync();
        Task<TipVozila> GetTipVozilaByIdAsync(int id);
        Task AddTipVozilaAsync(TipVozila tipVozila);
        Task UpdateTipVozilaAsync(TipVozila tipVozila);
        Task DeleteTipVozilaAsync(int id);
	}
}

