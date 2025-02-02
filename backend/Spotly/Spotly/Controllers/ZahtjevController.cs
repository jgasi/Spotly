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

        [HttpGet("nacekanju")]
        public async Task<ActionResult<IEnumerable<Zahtjev>>> GetAllZahtjeviNaCekanjuAsync()
        {
            var zahtjevi = await _zahtjevService.GetAllZahtjeviNaCekanjuAsync();

            if (zahtjevi == null || !zahtjevi.Any())
            {
                return NotFound("Nema dostupnih zahtjeva.");
            }

            return Ok(zahtjevi);
        }

        [HttpGet("paginated")]
        public async Task<ActionResult<IEnumerable<ZahtjevDto>>> GetPagedZahtjeviAsync(int pageNumber, int pageSize)
        {
            var zahtjevi = await _zahtjevService.GetPagedZahtjeviAsync(pageNumber, pageSize);

            if (zahtjevi == null || !zahtjevi.Any())
            {
                return NotFound("Nema dostupnih zahtjeva.");
            }

            return Ok(zahtjevi);
        }

        [HttpGet("paginated_na_cekanju")]
        public async Task<ActionResult<IEnumerable<ZahtjevDto>>> GetPagedZahtjeviNaCekanjuAsync(int pageNumber, int pageSize)
        {
            var zahtjevi = await _zahtjevService.GetPagedZahtjeviNaCekanjuAsync(pageNumber, pageSize);

            if (zahtjevi == null || !zahtjevi.Any())
            {
                return NotFound("Nema dostupnih zahtjeva na čekanju.");
            }

            return Ok(zahtjevi);
        }

        [HttpGet("paginated_odgovoreni")]
        public async Task<ActionResult<IEnumerable<ZahtjevDto>>> GetPagedZahtjeviOdgovorenAsync(int pageNumber, int pageSize)
        {
            var zahtjevi = await _zahtjevService.GetPagedZahtjeviOdgovoreniAsync(pageNumber, pageSize);

            if (zahtjevi == null || !zahtjevi.Any())
            {
                return NotFound("Nema dostupnih zahtjeva sa statusom Odgovoren.");
            }

            return Ok(zahtjevi);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ZahtjevDto>> GetZahtjevByIdAsync(int id)
        {
            var zahtjev = await _zahtjevService.GetZahtjevByIdAsync(id);

            if(zahtjev == null)
            {
                return NotFound($"Zahtjev s ID {id} ne postoji.");
            }

            return Ok(zahtjev);
        }

        [HttpGet("korid/{idkor}")]
        public async Task<ActionResult<ZahtjevDto>> GetZahtjevByKorisnikIdAsync(int idkor)
        {
            var zahtjev = await _zahtjevService.GetZahtjevByKorisnikIdAsync(idkor);

            if (zahtjev == null)
            {
                return NotFound($"Zahtjev s korisnik ID {idkor} ne postoji.");
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
        public async Task<ActionResult> UpdateZahtjevAsync(ZahtjevDto zahtjev)
        {
            if (zahtjev == null)
            {
                return BadRequest("Podaci za zahtjev nisu valjani");
            }

            // Hardkodirani datum jer trenutno nesto ne prihvaca datume json??
            //zahtjev.DatumVrijeme = new DateTime(2024, 11, 19, 12, 0, 0);

            await _zahtjevService.UpdateZahtjevAsync(zahtjev);


            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteZahtjevAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _zahtjevService.GetZahtjevByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Zahtjev s ID {id} ne postoji.");
            }

            await _zahtjevService.DeleteZahtjevAsync(id);

            return Ok();
        }
    }
}
