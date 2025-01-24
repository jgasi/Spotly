using Spotly.Data.Repositories;
using Spotly.DTOs;
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

        public async Task<IEnumerable<ZahtjevDto>> GetAllZahtjeviNaCekanjuAsync()
        {
            return await _zahtjevRepository.GetZahtjeviNaCekanjuAsync();
        }

        public async Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviAsync(int pageNumber, int pageSize)
        {
            if (pageNumber < 1 || pageSize < 1)
            {
                throw new ArgumentException("Neispravni parametri za stranicu ili broj zahtjeva.");
            }

            return await _zahtjevRepository.GetPagedAsync(pageNumber, pageSize);
        }

        public async Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviNaCekanjuAsync(int pageNumber, int pageSize)
        {
            if (pageNumber < 1 || pageSize < 1)
            {
                throw new ArgumentException("Neispravni parametri za stranicu ili broj zahtjeva.");
            }

            return await _zahtjevRepository.GetPagedNaCekanjuAsync(pageNumber, pageSize);
        }


        public async Task<ZahtjevDto> GetZahtjevByIdAsync(int id)
        {
            return await _zahtjevRepository.GetZahtjevByIdAsync(id);
        }

        public async Task<IEnumerable<ZahtjevDto>> GetZahtjevByKorisnikIdAsync(int id)
        {
            return await _zahtjevRepository.GetZahtjevByKorisnikIdAsync(id);
        }

        public async Task AddZahtjevAsync(Zahtjev zahtjev)
        {
            await _zahtjevRepository.AddAsync(zahtjev);
        }

        public async Task UpdateZahtjevAsync(ZahtjevDto zahtjev)
        {
            await _zahtjevRepository.UpdateAsync(zahtjev);
        }

        public async Task DeleteZahtjevAsync(int id)
        {
            await _zahtjevRepository.DeleteAsync(id);
        }

        public async Task<IEnumerable<ZahtjevDto>> GetPagedZahtjeviOdgovoreniAsync(int pageNumber, int pageSize)
        {
            if (pageNumber < 1 || pageSize < 1)
            {
                throw new ArgumentException("Neispravni parametri za stranicu ili broj zahtjeva.");
            }

            return await _zahtjevRepository.GetPagedOdgovoreniAsync(pageNumber, pageSize);
        }
    }
}
