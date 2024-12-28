using Azure.Core;
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
        public async Task<ActionResult<ParkingMjesto>> GetParkingMjestoByID(int id)
        {
            var parkingMjesto = await _parkingMjestoService.GetParkingMjestoById(id);

            if(parkingMjesto == null)
            {
                return NotFound("Ovo parking mjesto ne postoji.");
            }

            return Ok(parkingMjesto);
        }

        [HttpPost]
        public async Task<ActionResult> ReserveParkingSpace([FromBody] RezervacijaDto request)
        {
            if (request.Id <= 0 ||
    request.Datum_vrijeme_rezervacije == default(DateTime) ||
    request.Datum_vrijeme_odlaska == default(DateTime) ||
    !request.Parking_mjestoId.HasValue ||
    !request.VoziloId.HasValue)
            {
                return BadRequest("Sva polja su obavezna.");
            }

            var parkingSpace = await _parkingMjestoService.GetParkingMjestoById((int)request.Parking_mjestoId);
            if(parkingSpace == null)
            {
                return BadRequest("Parking mjesto ne postoji.");
            }
            
            if(parkingSpace.Status == "zauzeto")
            {
                return BadRequest("Parking mjesto je već zauzeto.");
            }

            if(parkingSpace.Dostupnost == "zauzeto")
            {
                return BadRequest("Parking mjesto je već zauzeto.");
            }
        }
    }
}
