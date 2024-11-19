using Spotly.Data.Repositories;
using Spotly.Models;

namespace Spotly.Services
{
    public class DokumentacijaService : IDokumentacijaService
    {
        private readonly IDokumentacijaRepository _dokumentacijaRepository;

        public DokumentacijaService(IDokumentacijaRepository dokumentacijaRepository)
        {
            _dokumentacijaRepository = dokumentacijaRepository;
        }

        public async Task<IEnumerable<Dokumentacija>> GetAllDokumentacijuAsync()
        {
            return await _dokumentacijaRepository.GetAllAsync();
        }

        public async Task<Dokumentacija> GetDokumentacijuByIdAsync(int id)
        {
            return await _dokumentacijaRepository.GetByIdAsync(id);
        }

        public async Task AddDokumentacijuAsync(Dokumentacija dokumentacija)
        {
            await _dokumentacijaRepository.AddAsync(dokumentacija);
        }

        public async Task UpdateDokumentacijuAsync(Dokumentacija dokumentacija)
        {
            await _dokumentacijaRepository.UpdateAsync(dokumentacija);
        }

        public async Task DeleteDokumentacijuAsync(int id)
        {
            await _dokumentacijaRepository.DeleteAsync(id);
        } 
    }
}
