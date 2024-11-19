using Spotly.Data.Repositories;
using Spotly.Models;

namespace Spotly.Services
{
    public class ZahtjevService : IZahtjevService
    {
        private readonly IZahtjevRepository _zahtjevRepository;

        public ZahtjevService(IZahtjevRepository zahtjevRepository)
        {
            _zahtjevRepository = zahtjevRepository;
        }

        public async Task<IEnumerable<Zahtjev>> GetAllZahtjeviAsync()
        {
            return await _zahtjevRepository.GetAllAsync();
        }

        public async Task<Zahtjev> GetZahtjevByIdAsync(int id)
        {
            return await _zahtjevRepository.GetByIdAsync(id);
        }

        public async Task AddZahtjevAsync(Zahtjev zahtjev)
        {
            await _zahtjevRepository.AddAsync(zahtjev);
        }

        public async Task UpdateZahtjevAsync(Zahtjev zahtjev)
        {
            await _zahtjevRepository.UpdateAsync(zahtjev);
        }

        public async Task DeleteZahtjevAsync(int id)
        {
            await _zahtjevRepository.DeleteAsync(id);
        }    
    }
}
