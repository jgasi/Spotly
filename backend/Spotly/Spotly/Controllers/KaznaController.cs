using Microsoft.AspNetCore.Mvc;
using Spotly.DTOs;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class KaznaController : ControllerBase
    {
        private readonly IKaznaService _kaznaService;

        public KaznaController(IKaznaService kaznaService)
        {
            _kaznaService = kaznaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Kazna>>> GetAllKazneAsync()
        {
            var kazne = await _kaznaService.GetAllKazneAsync();

            if (kazne == null || !kazne.Any())
            {
                return NotFound("Nema dostupnih kazni.");
            }

            return Ok(kazne);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Kazna>> GetKaznuByIdAsync(int id)
        {
            var kazna = await _kaznaService.GetKazneByIdAsync(id);

            if (kazna == null)
            {
                return NotFound($"Kazna s ID {id} ne postoji.");
            }

            return Ok(kazna);
        }

        [HttpGet("user/{korisnikId}")]
        public async Task<ActionResult<IEnumerable<KaznaDto>>> GetKazneByUserIdAsync(int korisnikId)
        {
            var kazne = await _kaznaService.GetKazneByUserIdAsync(korisnikId);

            if (kazne == null)
            {
                return NotFound($"Korisnik s ID {korisnikId} nema kazne.");
            }

            return Ok(kazne);
        }

        [HttpPost]
        public async Task<ActionResult> AddKaznuAsync(Kazna kazna)
        {
            if (kazna == null)
            {
                return BadRequest("Podaci za kaznu nisu valjani");
            }

            // Hardkodirani datum jer trenutno nesto ne prihvaca datume json??
            kazna.DatumVrijeme = new DateTime(2024, 11, 19, 12, 0, 0);

            await _kaznaService.AddKaznuAsync(kazna);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateKaznuAsync(Kazna kazna)
        {
            if (kazna == null)
            {
                return BadRequest("Podaci za kaznu nisu valjani");
            }

            // Hardkodirani datum jer trenutno nesto ne prihvaca datume json??
            kazna.DatumVrijeme = new DateTime(2024, 11, 19, 12, 0, 0);

            await _kaznaService.UpdateKaznuAsync(kazna);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteKaznuAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _kaznaService.GetKazneByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Kazna s ID {id} ne postoji.");
            }

            await _kaznaService.DeleteKaznuAsync(id);

            return Ok();
        }

        [HttpGet("statistics/total")]
        public async Task<ActionResult<int>> GetTotalKazneCountAsync()
        {
            var totalKazne = await _kaznaService.GetTotalKazneCountAsync();
            return Ok(totalKazne);
        }

        [HttpGet("statistics/user/{korisnikId}")]
        public async Task<ActionResult<int>> GetKazneCountByUserIdAsync(int korisnikId)
        {
            var userKazneCount = await _kaznaService.GetKazneCountByUserIdAsync(korisnikId);
            return Ok(userKazneCount);
        }
    }
}
