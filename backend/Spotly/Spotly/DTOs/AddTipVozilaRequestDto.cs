using System.Text.Json.Serialization;

namespace Spotly.DTOs
{
	public class AddTipVozilaRequestDto
	{
        [JsonPropertyName("Tip")]
        public string Tip { get; set; } = null!;
    }
}

