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
    public class ParkingMjestoController : ControllerBase
    {
        private readonly IParkingMjestoService _parkingMjestoService;

        public ParkingMjestoController(IParkingMjestoService parkingMjestoService)
        {
            _parkingMjestoService = parkingMjestoService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ParkingMjesto>>> GetAllParkingMjestaAsync()
        {
            var parkingMjesta = await _parkingMjestoService.GetAllParkingMjestaAsync();

            if (parkingMjesta == null || !parkingMjesta.Any())
            {
                return NotFound("Nema parking mjesta.");
            }

            return Ok(parkingMjesta);
        }

        [HttpGet("statistics")]
        public async Task<ActionResult> GetParkingStatisticsAsync()
        {
            var total = await _parkingMjestoService.GetTotalParkingMjestaAsync();
            var available = await _parkingMjestoService.GetAvailableParkingMjestaAsync();
            var reserved = await _parkingMjestoService.GetReservedParkingMjestaAsync();
            var blocked = await _parkingMjestoService.GetBlockedParkingMjestaAsync();

            return Ok(new
            {
                Ukupno = total,
                Slobodna = available,
                Rezervirana = reserved,
                Blokirana = blocked
            });
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ParkingMjesto>> GetParkingMjestoByIdAsync(int id)
        {
            var parkingMjesto = await _parkingMjestoService.GetParkingMjestoByIdAsync(id);

            if (parkingMjesto == null)
            {
                return NotFound($"Parking mjesto s ID {id} ne postoji.");
            }

            return Ok(parkingMjesto);
        }

        [HttpPut]
        public async Task<ActionResult> UpdateBlockStateAsync(ParkingMjesto parkingMjesto)
        {
            if (parkingMjesto == null)
            {
                return BadRequest("Podaci za parking mjesto nisu valjani");
            }

            await _parkingMjestoService.UpdateBlockStateAsync(parkingMjesto);

            return Ok();
        }
    }
}
