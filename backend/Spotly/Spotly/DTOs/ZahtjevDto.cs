namespace Spotly.DTOs
{
    public class ZahtjevDto
    {
        public int Id { get; set; }

        public string Predmet { get; set; } = string.Empty;

        public string Poruka { get; set; } = string.Empty;

        public string? Odgovor { get; set; }

        public string? Status { get; set; }

        public DateTime DatumVrijeme { get; set; }

        public int? AdminId { get; set; }

        public int? KorisnikId { get; set; }

        public int? TipZahtjevaId { get; set; }
    }

}
