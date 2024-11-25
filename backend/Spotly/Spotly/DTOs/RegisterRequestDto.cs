using System.Text.Json.Serialization;

namespace Spotly.DTOs
{
    public class RegisterRequestDto
    {
        [JsonPropertyName("ime")]
        public string Ime { get; set; } = null!;

        [JsonPropertyName("prezime")]
        public string Prezime { get; set; } = null!;

        [JsonPropertyName("email")]
        public string Email { get; set; } = null!;

        [JsonPropertyName("lozinka")]
        public string Lozinka { get; set; } = null!;

        [JsonPropertyName("brojMobitela")]
        public string? BrojMobitela { get; set; }

        [JsonPropertyName("tipKorisnikaId")]
        public int TipKorisnikaId { get; set; } 
    }
}
