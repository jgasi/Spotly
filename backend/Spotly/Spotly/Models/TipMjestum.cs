using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipMjestum
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<ParkingMjesto> ParkingMjestos { get; set; } = new List<ParkingMjesto>();
}
