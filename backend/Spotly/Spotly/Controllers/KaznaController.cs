﻿using Microsoft.AspNetCore.Mvc;
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
    }
}
