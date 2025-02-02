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
            if (parkingSpace == null)
            {
                return BadRequest("Parking mjesto ne postoji.");
            }

            int voziloId = (int)request.VoziloId;
            int parkingMjestoId = (int)request.Parking_mjestoId;


            var existingReservation = await _rezervacijaService.GetRezervacijaByVoziloAndParkingAsync(
                voziloId, parkingMjestoId);

            if (existingReservation != null)
            {
                existingReservation.DatumVrijemeOdlaska = request.Datum_vrijeme_odlaska;
                await _rezervacijaService.UpdateRezervacijaAsync(existingReservation);

                parkingSpace.Status = "Slobodno";
                parkingSpace.Dostupnost = "Slobodno";
                await _parkingMjestoService.UpdateParkingMjesto(parkingSpace);

                return Ok("Rezervacija ažurirana.");
            }

            if (parkingSpace.Status == "Zauzeto" || parkingSpace.Dostupnost == "Zauzeto")
            {
                return BadRequest("Parking mjesto je već zauzeto.");
            }

            var newReservation = new Rezervacija
            {
                DatumVrijemeRezervacije = request.Datum_vrijeme_rezervacije,
                DatumVrijemeOdlaska = request.Datum_vrijeme_odlaska,
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
