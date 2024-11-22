namespace Spotly.DTOs
{
    public class RegisterRequestDto
    {
        public string Ime { get; set; } = null!;
        public string Prezime { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Lozinka { get; set; } = null!;
        public string? BrojMobitela { get; set; }
        public int TipKorisnikaId { get; set; } 
    }
}
