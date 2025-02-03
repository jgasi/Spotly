using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Spotly.DTOs;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize]
    public class RezervacijaController : ControllerBase
    {
        private readonly IRezervacijaService _rezervacijaService;

        public RezervacijaController(IRezervacijaService rezervacijaService)
        {
            _rezervacijaService = rezervacijaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Rezervacija>>> GetAllRezervacijeAsync()
        {
            var rezervacije = await _rezervacijaService.GetAllRezervacijeAsync();

            if (rezervacije == null || !rezervacije.Any())
            {
                return NotFound("Nema dostupnih rezervacija.");
            }

            return Ok(rezervacije);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<RezervacijaDto>> GetRezervacijaByIdAsync(int id)
        {
            var rezervacija = await _rezervacijaService.GetRezervacijaByIdAsync(id);

            if (rezervacija == null)
            {
                return NotFound($"Rezervacija s ID {id} ne postoji.");
            }

            return Ok(rezervacija);
        }

        [HttpGet("vozilo/{id}")]
        public async Task<ActionResult<RezervacijaDto>> GetRezervacijaByVoziloIdAsync(int id)
        {
            var rezervacija = await _rezervacijaService.GetRezervacijaByVoziloIdAsync(id);

            if (rezervacija == null)
            {
                return NotFound($"Rezervacija s VoziloID {id} ne postoji.");
            }

            return Ok(rezervacija);
        }

        [HttpPost]
        public async Task<ActionResult> AddRezervacijaAsync(Rezervacija rezervacija)
        {
            if (rezervacija == null)
            {
                return BadRequest("Podaci za rezervaciju nisu valjani.");
            }

            await _rezervacijaService.AddRezervacijaAsync(rezervacija);
            return Ok();
        }

        [HttpPut]
        public async Task<ActionResult> UpdateRezervacijaAsync(Rezervacija rezervacija)
        {
            if (rezervacija == null)
            {
                return BadRequest("Podaci za rezervaciju nisu valjani.");
            }

            await _rezervacijaService.UpdateRezervacijaAsync(rezervacija);
            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteRezervacijaAsync(int id)
        {
            if (id <= 0)
            {
                return BadRequest("Nije poslan ispravan ID za brisanje.");
            }

            var rezervacija = await _rezervacijaService.GetRezervacijaByIdAsync(id);

            if (rezervacija == null)
            {
                return NotFound($"Rezervacija s ID {id} ne postoji.");
            }

            await _rezervacijaService.DeleteRezervacijaAsync(id);
            return Ok();
        }
    }
}
