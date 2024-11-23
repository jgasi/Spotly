using Spotly.Models;

namespace Spotly.Services
{
    public interface IKorisnikService
    {
        Task<IEnumerable<Korisnik>> GetAllKorisnikeAsync();
        Task<Korisnik> GetKorisnikByIdAsync(int id);
        Task AddKorisnikaAsync(Korisnik korisnik);
        Task UpdateKorisnikaAsync(Korisnik korisnik);
        Task DeleteKorisnikaAsync(int id);
        Task<Korisnik> GetKorisnikByEmailAsync(string email);

        Task<IEnumerable<TipKorisnika>> GetAllTipoviKorisnikaAsync();
    }
}
