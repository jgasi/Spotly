using Microsoft.AspNetCore.Mvc;
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
