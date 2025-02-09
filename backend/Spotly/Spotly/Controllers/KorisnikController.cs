﻿using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Spotly.Models;
using Spotly.Services;
using Spotly.DTOs;

namespace Spotly.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize]
    public class KorisnikController : ControllerBase
    {
        private readonly IKorisnikService _korisnikService;
        private readonly IConfiguration _configuration;

        public KorisnikController(IKorisnikService korisnikService, IConfiguration configuration) 
        {
            _korisnikService = korisnikService;
            _configuration = configuration;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Korisnik>>> GetAllKorisnikeAsync()
        {
            var korisnici = await _korisnikService.GetAllKorisnikeAsync();

            if (korisnici == null || !korisnici.Any())
            {
                return NotFound("Nema dostupnih korisnika.");
            }

            return Ok(korisnici);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Korisnik>> GetKorisnikaByIdAsync(int id)
        {
            var korisnik = await _korisnikService.GetKorisnikByIdAsync(id);

            if (korisnik == null)
            {
                return NotFound($"Korisnik s ID {id} ne postoji.");
            }

            return Ok(korisnik);
        }

        [HttpPost]
        public async Task<ActionResult> AddKorisnikaAsync(Korisnik korisnik)
        {
            if (korisnik == null)
            {
                return BadRequest("Podaci za korisnika nisu valjani");
            }

            await _korisnikService.AddKorisnikaAsync(korisnik);
            return Ok();
        }


        [HttpPut]
        public async Task<ActionResult> UpdateKorisnikaAsync(Korisnik korisnik)
        {
            if (korisnik == null)
            {
                return BadRequest("Podaci za korisnika nisu valjani");
            }

            await _korisnikService.UpdateKorisnikaAsync(korisnik);

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteKorisnikaAsync(int id)
        {
            if (id == null)
            {
                return BadRequest("Nije poslan ID za brisanje.");
            }

            var forDelete = await _korisnikService.GetKorisnikByIdAsync(id);

            if (forDelete == null)
            {
                return BadRequest($"Korisnik s ID {id} ne postoji.");
            }

            await _korisnikService.DeleteKorisnikaAsync(id);

            return Ok();
        }

        [HttpPost("register")]
        public async Task<ActionResult> RegisterAsync([FromBody] RegisterRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Ime) ||
                string.IsNullOrWhiteSpace(request.Prezime) ||
                string.IsNullOrWhiteSpace(request.Email) ||
                string.IsNullOrWhiteSpace(request.Lozinka))
            {
                return BadRequest("Ime, Prezime, Email i Lozinka su obavezni.");
            }

            var existingUser = await _korisnikService.GetKorisnikByEmailAsync(request.Email);
            if (existingUser != null)
            {
                return BadRequest("Korisnik s tim emailom već postoji.");
            }

            var newUser = new Korisnik
            {
                Ime = request.Ime,
                Prezime = request.Prezime,
                Email = request.Email,
                Lozinka = BCrypt.Net.BCrypt.HashPassword(request.Lozinka),
                BrojMobitela = request.BrojMobitela,
                Status = "Aktivan",
                TipKorisnikaId = request.TipKorisnikaId
            };

            await _korisnikService.AddKorisnikaAsync(newUser);
            return Ok();
        }

        [HttpPost("login")]
        [AllowAnonymous]
        public async Task<ActionResult> LoginAsync([FromBody] LoginRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Lozinka))
            {
                return BadRequest("Email i Lozinka su obavezni.");
            }

            var korisnik = await _korisnikService.GetKorisnikByEmailAsync(request.Email);
            if (korisnik == null)
            {
                return Unauthorized("Neispravni email ili lozinka.");
            }

            var isPasswordValid = BCrypt.Net.BCrypt.Verify(request.Lozinka, korisnik.Lozinka);
            if (!isPasswordValid)
            {
                return Unauthorized("Neispravni email ili lozinka.");
            }

            if (korisnik.Status.ToLower() != "aktivan")
            {
                return Unauthorized("Račun korisnika nije aktivan.");
            }
            
            var token = GenerateJwtToken(korisnik);

            return Ok(new
            {
                Message = "Prijava uspješna.",
                User = new
                {
                    korisnik.Id,
                    korisnik.Ime,
                    korisnik.Prezime,
                    korisnik.Email,
                    korisnik.TipKorisnikaId,
                    token
                }
            });
        }

        [HttpGet("user-types")]
        public async Task<ActionResult<IEnumerable<TipKorisnika>>> GetAllTipoviKorisnikaAsync()
        {
            var tipoviKorisnika = await _korisnikService.GetAllTipoviKorisnikaAsync();

            if (tipoviKorisnika == null || !tipoviKorisnika.Any())
            {
                return NotFound("Nema dostupnih tipova korisnika.");
            }

            return Ok(tipoviKorisnika);
        }
        [HttpGet("user-types/{id}")]
        public async Task<ActionResult<IEnumerable<TipKorisnika>>> GetTipKorisnikaAsync(int id)
        {
            var tipKorisnika = await _korisnikService.GetTipKorisnikaAsync(id);

            if (tipKorisnika == null)
            {
                return NotFound("Nema dostupnog tipa korisnika.");
            }

            return Ok(tipKorisnika);
        }

        [HttpGet("user-types-by-userid/{id}")]
        public async Task<ActionResult<IEnumerable<TipKorisnikaDto>>> GetTipKorisnikaByKorisnikIdAsync(int id)
        {
            var tipKorisnika = await _korisnikService.GetTipKorisnikaByKorisnikIdAsync(id);

            if (tipKorisnika == null)
            {
                return NotFound($"Nema dostupnog tipa korisnika s korisnik ID {id}");
            }

            return Ok(tipKorisnika);
        }

        private string GenerateJwtToken(Korisnik korisnik)
        {
            var claims = new[]
            {
                new Claim(ClaimTypes.NameIdentifier, korisnik.Id.ToString()),
                new Claim(ClaimTypes.Email, korisnik.Email),
                new Claim(ClaimTypes.Role, korisnik.TipKorisnikaId.ToString())
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));
            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: _configuration["Jwt:Issuer"],
                audience: _configuration["Jwt:Audience"],
                claims: claims,
                expires: DateTime.Now.AddHours(1),
                signingCredentials: credentials
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

    }
}
