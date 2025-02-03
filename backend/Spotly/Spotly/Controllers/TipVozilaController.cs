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
    public class TipVozilaController : ControllerBase
    {
        private readonly ITipVozilaService _tipVozilaService;

        public TipVozilaController(ITipVozilaService tipVozilaService)
        {
            _tipVozilaService = tipVozilaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<TipVozila>>> GetAllTipVozilaAsync()
        {
            var tipoviVozila = await _tipVozilaService.GetAllTipVozilaAsync();

            if (tipoviVozila == null || !tipoviVozila.Any())
            {
                return NotFound("Nema dostupnih tipova vozila.");
            }

            return Ok(tipoviVozila);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Vozilo>> GetTipVozilaByIdAsync(int id)
        {
            var tip = await _tipVozilaService.GetTipVozilaByIdAsync(id);

            if (tip == null)
            {
                return NotFound($"Tip vozila s ID {id} ne postoji.");
            }

            return Ok(tip);
        }

        [HttpPost]
        public async Task<ActionResult> AddTipVozilaAsync(AddTipVozilaRequestDto tipVozila)
        {
            if (tipVozila == null)
            {
                return BadRequest("Podaci za tip vozila nisu valjani");
            }

            var newTipVozila = new TipVozila
            {
                Tip = tipVozila.Tip
            };

            try
            {
                await _tipVozilaService.AddTipVozilaAsync(newTipVozila);
            } catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }

            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateTipVozilaAsync(TipVozila tipVozila)
        {
            if (tipVozila == null)
            {
                return BadRequest("Podaci za tip vozila nisu valjani");
            }

            await _tipVozilaService.UpdateTipVozilaAsync(tipVozila);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteTipVozilaAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _tipVozilaService.GetTipVozilaByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Tip vozila s ID {id} ne postoji.");
            }

            await _tipVozilaService.DeleteTipVozilaAsync(id);

            return Ok();
        }
    }
}

