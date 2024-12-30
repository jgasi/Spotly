using Microsoft.AspNetCore.Mvc;
using Spotly.DTOs;
using Spotly.Models;
using Spotly.Services;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
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
    }
}
