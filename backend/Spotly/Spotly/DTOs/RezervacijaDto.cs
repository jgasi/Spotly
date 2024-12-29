namespace Spotly.DTOs
{
    public class RezervacijaDto
    {
        public int Id { get; set; }
        public DateTime Datum_vrijeme_rezervacije{ get; set; } = DateTime.Now!;
        public DateTime Datum_vrijeme_odlaska { get; set; } = DateTime.Now!;
        public int? VoziloId { get; set; }
        public int? Parking_mjestoId { get; set; }
    }
}
