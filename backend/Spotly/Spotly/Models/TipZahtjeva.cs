using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipZahtjeva
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<Zahtjev> Zahtjevs { get; set; } = new List<Zahtjev>();
}
