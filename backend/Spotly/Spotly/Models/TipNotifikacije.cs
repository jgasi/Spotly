using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipNotifikacije
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<Notifikacija> Notifikacijas { get; set; } = new List<Notifikacija>();
}
