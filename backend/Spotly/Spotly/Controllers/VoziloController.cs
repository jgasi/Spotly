using Microsoft.AspNetCore.Mvc;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class VoziloController : ControllerBase
    {
        private readonly IVoziloService _voziloService;

        public VoziloController(IVoziloService voziloService)
        {
            _voziloService = voziloService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Vozilo>>> GetAllVozilaAsync()
        {
            var vozila = await _voziloService.GetAllVozilaAsync();

            if (vozila == null || !vozila.Any())
            {
                return NotFound("Nema dostupnih vozila.");
            }

            return Ok(vozila);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Vozilo>> GetVoziloByIdAsync(int id)
        {
            var vozilo = await _voziloService.GetVoziloByIdAsync(id);

            if (vozilo == null)
            {
                return NotFound($"Vozilo s ID {id} ne postoji.");
            }

            return Ok(vozilo);
        }

        [HttpGet("korisnik/{id}")]
        public async Task<ActionResult<Vozilo>> GetVoziloByKorisnikIdAsync(int id)
        {
            var vozilo = await _voziloService.GetVoziloByKorisnikIdAsync(id);

            if (vozilo == null)
            {
                return NotFound($"Korisnik {id} nema niti jednone vozilo u bazi.");
            }

            return Ok(vozilo);
        }

        [HttpGet("vehicles/lookup")]
        public async Task<ActionResult> LookupVehiclesByLicensePlateAsync(string licensePlate)
        {
            var vozilo = await _voziloService.GetVoziloByLicensePlateAsync(licensePlate);

            if (vozilo == null)
            {
                return NotFound(new
                {
                    success = false,
                    status = 404,
                    message = $"Vozilo s registarskom oznakom {licensePlate} ne postoji",
                    data = new[] { "" }
                });
            }

            return Ok(new
            {
                success = true,
                status = 200,
                message = "Pronađeno vozilo",
                data = new[] { vozilo }
            });

        }

        [HttpPost]
        public async Task<ActionResult> AddVoziloAsync(Spotly.DTOs.AddVoziloDto vozilo)
        {
            if (vozilo == null)
            {
                return BadRequest("Podaci za vozilo nisu valjani");
            }

            var newVozilo = new Vozilo
            {
                Godiste = vozilo.Godiste,
                Marka = vozilo.Marka,
                Registracija = vozilo.Registracija,
                Model = vozilo.Model,
                KorisnikId = vozilo.KorisnikId,
                TipVozilaId = vozilo.TipVozilaId
            };

            await _voziloService.AddVoziloAsync(newVozilo);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateVoziloAsync(Vozilo vozilo)
        {
        if (vozilo == null)
            {
                return BadRequest("Podaci za vozilo nisu valjani");
            }

            await _voziloService.UpdateVoziloAsync(vozilo);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteVoziloAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _voziloService.GetVoziloByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Vozilo s ID {id} ne postoji.");
            }

            await _voziloService.DeleteVoziloAsync(id);

            return Ok();
        }
    }
}

