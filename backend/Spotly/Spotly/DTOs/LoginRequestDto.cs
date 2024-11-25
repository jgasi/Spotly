using System.Text.Json.Serialization;

public class LoginRequestDto {

    [JsonPropertyName("email")]
    public string Email { get; set; }

    [JsonPropertyName("lozinka")]
    public string Lozinka { get; set; }
}