using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class ParkingMjesto
{
    public int Id { get; set; }

    public string Status { get; set; } = null!;

    public string? Dostupnost { get; set; }

    public int? TipMjestaId { get; set; }

    public virtual ICollection<Rezervacija> Rezervacijas { get; set; } = new List<Rezervacija>();

    public virtual TipMjestum? TipMjesta { get; set; }
}
