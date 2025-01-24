using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize]
    public class DokumentacijaController : ControllerBase
    {
        private readonly IDokumentacijaService _dokumentacijaService;

        public DokumentacijaController(IDokumentacijaService dokumentacijaService)
        {
            _dokumentacijaService = dokumentacijaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Dokumentacija>>> GetAllDokumentacijuAsync()
        {
            var dokumentacija = await _dokumentacijaService.GetAllDokumentacijuAsync();

            if (dokumentacija == null || !dokumentacija.Any())
            {
                return NotFound("Nema dostupne dokumentacije.");
            }

            return Ok(dokumentacija);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Dokumentacija>> GetDokumentacijuByIdAsync(int id)
        {
            var dokumentacija = await _dokumentacijaService.GetDokumentacijuByIdAsync(id);

            if (dokumentacija == null)
            {
                return NotFound($"Dokumentacija s ID {id} ne postoji.");
            }

            return Ok(dokumentacija);
        }

        [HttpPost]
        public async Task<ActionResult> AddDokumentacijuAsync(Dokumentacija dokumentacija)
        {
            if (dokumentacija == null)
            {
                return BadRequest("Podaci za dokumentaciju nisu valjani");
            }

            await _dokumentacijaService.AddDokumentacijuAsync(dokumentacija);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateDokumentacijuAsync(Dokumentacija dokumentacija)
        {
            if (dokumentacija == null)
            {
                return BadRequest("Podaci za dokumentaciju nisu valjani");
            }

            await _dokumentacijaService.UpdateDokumentacijuAsync(dokumentacija);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteDokumentacijuAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _dokumentacijaService.GetDokumentacijuByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Dokumentacija s ID {id} ne postoji.");
            }

            await _dokumentacijaService.DeleteDokumentacijuAsync(id);

            return Ok();
        }
    }
}
