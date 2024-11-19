using Microsoft.AspNetCore.Mvc;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ZahtjevController : ControllerBase
    {
        private readonly IZahtjevService _zahtjevService;

        public ZahtjevController(IZahtjevService zahtjevService)
        {
            _zahtjevService = zahtjevService;
        }


        [HttpGet]
        public async Task<ActionResult<IEnumerable<Zahtjev>>> GetAllZahtjeviAsync()
        {
            var zahtjevi = await _zahtjevService.GetAllZahtjeviAsync();

            if(zahtjevi == null || !zahtjevi.Any())
            {
                return NotFound("Nema dostupnih zahtjeva.");
            }

            return Ok(zahtjevi);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Zahtjev>> GetZahtjevByIdAsync(int id)
        {
            var zahtjev = await _zahtjevService.GetZahtjevByIdAsync(id);

            if(zahtjev == null)
            {
                return NotFound($"Zahtjev s ID {id} ne postoji.");
            }

            return Ok(zahtjev);
        }

        [HttpPost]
        public async Task<ActionResult> AddZahtjevAsync(Zahtjev zahtjev)
        {
            if (zahtjev == null)
            {
                return BadRequest("Podaci za zahtjev nisu valjani");
            }

            // Hardkodirani datum jer trenutno nesto ne prihvaca datume json??
            zahtjev.DatumVrijeme = new DateTime(2024, 11, 19, 12, 0, 0);

            await _zahtjevService.AddZahtjevAsync(zahtjev);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateZahtjevAsync(Zahtjev zahtjev)
        {
            if (zahtjev == null)
            {
                return BadRequest("Podaci za zahtjev nisu valjani");
            }

            // Hardkodirani datum jer trenutno nesto ne prihvaca datume json??
            zahtjev.DatumVrijeme = new DateTime(2024, 11, 19, 12, 0, 0);

            await _zahtjevService.UpdateZahtjevAsync(zahtjev);


            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult<Zahtjev>> DeleteZahtjevAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            await _zahtjevService.DeleteZahtjevAsync(id);

            var deletedZahtjev = _zahtjevService.GetZahtjevByIdAsync(id);

            return Ok();
        }
    }
}
