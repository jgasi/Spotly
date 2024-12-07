using System.Text.Json.Serialization;

namespace Spotly.DTOs
{
	public class AddVoziloDto
	{
        [JsonPropertyName("marka")]
        public string Marka { get; set; } = null!;

        [JsonPropertyName("model")]
        public string Model { get; set; } = null!;

        [JsonPropertyName("godiste")]
        public string Godiste { get; set; } = null!;

        [JsonPropertyName("registracija")]
        public string Registracija { get; set; } = null!;

        [JsonPropertyName("tipVozilaId")]
        public int TipVozilaId { get; set; }

        [JsonPropertyName("korisnikId")]
        public int KorisnikId { get; set; }
	}
}

