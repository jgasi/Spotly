using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public interface IKaznaService
    {
        Task<IEnumerable<Kazna>> GetAllKazneAsync();
        Task<Kazna> GetKazneByIdAsync(int id);
        Task<IEnumerable<KaznaDto>> GetKazneByUserIdAsync(int userId);
        Task AddKaznuAsync(Kazna kazna);
        Task UpdateKaznuAsync(Kazna kazna);
        Task DeleteKaznuAsync(int id);
    }
}
