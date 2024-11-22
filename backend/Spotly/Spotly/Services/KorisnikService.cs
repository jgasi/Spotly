using Spotly.Data.Repositories;
using Spotly.Models;

namespace Spotly.Services
{
    public class KorisnikService : IKorisnikService
    {
        private readonly IKorisnikRepository _korisnikRepository;

        public KorisnikService(IKorisnikRepository korisnikRepository)
        {
            _korisnikRepository = korisnikRepository;
        }

        public async Task<IEnumerable<Korisnik>> GetAllKorisnikeAsync()
        {
            return await _korisnikRepository.GetAllAsync();
        }

        public async Task<Korisnik> GetKorisnikByIdAsync(int id)
        {
            return await _korisnikRepository.GetByIdAsync(id);
        }

        public async Task<Korisnik> GetKorisnikByEmailAsync(string email)
        {
            return await _korisnikRepository.GetByEmailAsync(email);
        }

        public async Task AddKorisnikaAsync(Korisnik korisnik)
        {
            await _korisnikRepository.AddAsync(korisnik);
        }

        public async Task UpdateKorisnikaAsync(Korisnik korisnik)
        {
            await _korisnikRepository.UpdateAsync(korisnik);
        }

        public async Task DeleteKorisnikaAsync(int id)
        {
            await _korisnikRepository.DeleteAsync(id);
        }      
    }
}
