using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipVozila
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<Vozilo> Vozilos { get; set; } = new List<Vozilo>();
}
