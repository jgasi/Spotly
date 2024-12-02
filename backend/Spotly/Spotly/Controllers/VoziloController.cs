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

        [HttpPost]
        public async Task<ActionResult> AddVoziloAsync(Vozilo vozilo)
        {
            if (vozilo == null)
            {
                return BadRequest("Podaci za vozilo nisu valjani");
            }

            await _voziloService.AddVoziloAsync(vozilo);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateKorisnikaAsync(Vozilo vozilo)
        {
        if (vozilo == null)
            {
                return BadRequest("Podaci za vozilo nisu valjani");
            }

            await _voziloService.UpdateVoziloAsync(vozilo);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteKorisnikaAsync(int id)
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

