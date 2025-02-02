using Spotly.Data.Repositories;
using Spotly.DTOs;
using Spotly.Models;

namespace Spotly.Services
{
    public class KaznaService : IKaznaService
    {
        private readonly IKaznaRepository _kaznaRepository;

        public KaznaService(IKaznaRepository kaznaRepository)
        {
            _kaznaRepository = kaznaRepository;
        }

        public async Task<IEnumerable<Kazna>> GetAllKazneAsync()
        {
            return await _kaznaRepository.GetAllAsync();
        }

        public async Task<Kazna> GetKazneByIdAsync(int id)
        {
            return await _kaznaRepository.GetByIdAsync(id);
        }

        public async Task<IEnumerable<KaznaDto>> GetKazneByUserIdAsync(int userId)
        {
            return await _kaznaRepository.GetByKorisnikId(userId);
        }

        public async Task AddKaznuAsync(Kazna kazna)
        {
            await _kaznaRepository.AddAsync(kazna);
        }

        public async Task UpdateKaznuAsync(Kazna kazna)
        {
            await _kaznaRepository.UpdateAsync(kazna);
        }


        public async Task DeleteKaznuAsync(int id)
        {
            await _kaznaRepository.DeleteAsync(id);
        }

        public async Task<int> GetTotalKazneCountAsync()
        {
            return await _kaznaRepository.GetTotalKazneCountAsync();
        }

        public async Task<int> GetKazneCountByUserIdAsync(int userId)
        {
            return await _kaznaRepository.GetKazneCountByUserIdAsync(userId);
        }
    }
}
