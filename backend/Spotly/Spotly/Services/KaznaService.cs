﻿using Spotly.Data.Repositories;
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
    }
}
