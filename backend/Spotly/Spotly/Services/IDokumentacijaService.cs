using Spotly.Models;

namespace Spotly.Services
{
    public interface IDokumentacijaService
    {
        Task<IEnumerable<Dokumentacija>> GetAllDokumentacijuAsync();
        Task<Dokumentacija> GetDokumentacijuByIdAsync(int id);
        Task AddDokumentacijuAsync(Dokumentacija dokumentacija);
        Task UpdateDokumentacijuAsync(Dokumentacija dokumentacija);
        Task DeleteDokumentacijuAsync(int id);
    }
}
