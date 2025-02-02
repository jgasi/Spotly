using Microsoft.AspNetCore.Authorization;
using Azure.Core;
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
        private readonly IRezervacijaService _rezervacijaService;

        public ParkingMjestoController(IParkingMjestoService parkingMjestoService, IRezervacijaService rezervacijaService)
        {
            _parkingMjestoService = parkingMjestoService;
            _rezervacijaService = rezervacijaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ParkingMjesto>>> GetAllParkingMjestaAsync()
        {
            var parkingMjesta = await _parkingMjestoService.GetAllParkingMjestaIncludeAsync();
            //var parkingMjesta = await _parkingMjestoService.GetAllParkingMjestaAsync();

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

        [HttpPut("blokiraj/{id}")]
        public async Task<ActionResult> BlokirajParkingMjestoAsync(int id)
        {
            var uspjeh = await _parkingMjestoService.BlokirajParkingMjestoAsync(id);

            if (!uspjeh)
            {
                return NotFound($"Parking mjesto s ID {id} ne postoji.");
            }

            return Ok($"Parking mjesto s ID {id} je blokirano.");
        }

        [HttpPut("odblokiraj/{id}")]
        public async Task<ActionResult> OdblokirajParkingMjestoAsync(int id)
        {
            var uspjeh = await _parkingMjestoService.OdblokirajParkingMjestoAsync(id);

            if (!uspjeh)
            {
                return NotFound($"Parking mjesto s ID {id} ne postoji.");
            }

            return Ok($"Parking mjesto s ID {id} je odblokirano.");
        }

        [HttpPut]
        public async Task<ActionResult> UpdateBlockStateAsync(ParkingMjesto parkingMjesto)
        [HttpPut("blokiraj/{id}")]
        public async Task<ActionResult> BlokirajParkingMjestoAsync(int id)
        {
            var uspjeh = await _parkingMjestoService.BlokirajParkingMjestoAsync(id);

            if (!uspjeh)
            {
                return NotFound($"Parking mjesto s ID {id} ne postoji.");
            }

            return Ok($"Parking mjesto s ID {id} je blokirano.");
        }

        [HttpPut("odblokiraj/{id}")]
        public async Task<ActionResult> OdblokirajParkingMjestoAsync(int id)
        {
            var uspjeh = await _parkingMjestoService.OdblokirajParkingMjestoAsync(id);

            if (!uspjeh)
            {
                return NotFound($"Parking mjesto s ID {id} ne postoji.");
            }

            return Ok($"Parking mjesto s ID {id} je odblokirano.");
        }

        //[HttpGet("{id}")]
        //public async Task<ActionResult<ParkingMjesto>> GetParkingMjestoByID(int id)
        //{
        //    var parkingMjesto = await _parkingMjestoService.GetParkingMjestoById(id);

        //    if(parkingMjesto == null)
        //    {
        //        return NotFound("Ovo parking mjesto ne postoji.");
        //    }

        //    return Ok(parkingMjesto);
        //}

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
            if (parkingSpace == null)
            {
                return BadRequest("Parking mjesto ne postoji.");
            }

            int voziloId = (int)request.VoziloId;
            int parkingMjestoId = (int)request.Parking_mjestoId;


            var existingReservation = await _rezervacijaService.GetRezervacijaByVoziloAndParkingAsync(
                voziloId, parkingMjestoId);
            var latestReservation = await _rezervacijaService.GetLatestRezervacijaByVozilo(voziloId);

            if (existingReservation != null)
            {
                existingReservation.DatumVrijemeOdlaska = DateTime.Now;
                await _rezervacijaService.UpdateRezervacijaAsync(existingReservation);

                parkingSpace.Status = "Slobodno";
                parkingSpace.Dostupnost = "Slobodno";
                await _parkingMjestoService.UpdateParkingMjesto(parkingSpace);

                return Ok("Rezervacija ažurirana.");
            }

            if(latestReservation != null)
            {
                return BadRequest("Korisnik već ima rezervaciju.");
            }

            if (parkingSpace.Status == "Zauzeto" || parkingSpace.Dostupnost == "Zauzeto" || parkingSpace.Dostupnost == "Blokirano")
            {
                return BadRequest("Parking mjesto je već zauzeto.");
            }

            var newReservation = new Rezervacija
            {
                DatumVrijemeRezervacije = request.Datum_vrijeme_rezervacije,
                DatumVrijemeOdlaska = DateTime.Now.AddMonths(1),
                ParkingMjestoId = (int)request.Parking_mjestoId,
                VoziloId = (int)request.VoziloId
            };

            await _rezervacijaService.AddRezervacijaAsync(newReservation);

            parkingSpace.Status = "Zauzeto";
            parkingSpace.Dostupnost = "Zauzeto";
            await _parkingMjestoService.UpdateParkingMjesto(parkingSpace);

            return Ok("Rezervacija uspješno kreirana.");
        }
    }
}
