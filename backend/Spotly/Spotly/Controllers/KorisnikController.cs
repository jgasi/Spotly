using Microsoft.AspNetCore.Mvc;
using Spotly.Models;
using Spotly.Services;
using BCrypt.Net;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class KorisnikController : ControllerBase
    {
        private readonly IKorisnikService _korisnikService;

        public KorisnikController(IKorisnikService korisnikService) 
        {
            _korisnikService = korisnikService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Korisnik>>> GetAllKorisnikeAsync()
        {
            var korisnici = await _korisnikService.GetAllKorisnikeAsync();

            if (korisnici == null || !korisnici.Any())
            {
                return NotFound("Nema dostupnih korisnika.");
            }

            return Ok(korisnici);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Korisnik>> GetKorisnikaByIdAsync(int id)
        {
            var korisnik = await _korisnikService.GetKorisnikByIdAsync(id);

            if (korisnik == null)
            {
                return NotFound($"Korisnik s ID {id} ne postoji.");
            }

            return Ok(korisnik);
        }

        [HttpPost]
        public async Task<ActionResult> AddKorisnikaAsync(Korisnik korisnik)
        {
            if (korisnik == null)
            {
                return BadRequest("Podaci za korisnika nisu valjani");
            }

            await _korisnikService.AddKorisnikaAsync(korisnik);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateKorisnikaAsync(Korisnik korisnik)
        {
            if (korisnik == null)
            {
                return BadRequest("Podaci za korisnika nisu valjani");
            }

            await _korisnikService.UpdateKorisnikaAsync(korisnik);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteKorisnikaAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _korisnikService.GetKorisnikByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Korisnik s ID {id} ne postoji.");
            }

            await _korisnikService.DeleteKorisnikaAsync(id);

            return Ok();
        }

        [HttpPost("register")]
        public async Task<ActionResult> RegisterAsync([FromBody] RegisterRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.Ime) ||
                string.IsNullOrWhiteSpace(request.Prezime) ||
                string.IsNullOrWhiteSpace(request.Email) ||
                string.IsNullOrWhiteSpace(request.Lozinka))
            {
                return BadRequest("Ime, Prezime, Email i Lozinka su obavezni.");
            }

            var existingUser = await _korisnikService.GetKorisnikByEmailAsync(request.Email);
            if (existingUser != null)
            {
                return BadRequest("Korisnik s tim emailom već postoji.");
            }

            var newUser = new Korisnik
            {
                Ime = request.Ime,
                Prezime = request.Prezime,
                Email = request.Email,
                Lozinka = BCrypt.Net.BCrypt.HashPassword(request.Lozinka),
                BrojMobitela = request.BrojMobitela,
                Status = "Aktivan",
                TipKorisnikaId = request.TipKorisnikaId
            };

            await _korisnikService.AddKorisnikaAsync(newUser);
            return CreatedAtAction(nameof(GetKorisnikaByIdAsync), new { id = newUser.Id }, newUser);
        }

    }
}
