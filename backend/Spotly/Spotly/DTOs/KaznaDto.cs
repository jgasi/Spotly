namespace Spotly.DTOs
{
    public class KaznaDto
    {
        public int Id { get; set; }
        public string Razlog { get; set; } = null!;
        public string? NovcaniIznos { get; set; }
        public string DatumVrijeme { get; set; } = null!;
        public int? AdminId { get; set; }
        public int? KorisnikId { get; set; }
        public int? TipKazneId { get; set; }
    }
}
