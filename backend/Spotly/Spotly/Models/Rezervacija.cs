using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Rezervacija
{
    public int Id { get; set; }

    public DateTime DatumVrijemeRezervacije { get; set; }

    public DateTime? DatumVrijemeOdlaska { get; set; }

    public int? VoziloId { get; set; }

    public int? ParkingMjestoId { get; set; }

    public virtual ParkingMjesto? ParkingMjesto { get; set; }

    public virtual Vozilo? Vozilo { get; set; }
}
