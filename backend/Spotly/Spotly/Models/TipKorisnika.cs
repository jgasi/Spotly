using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipKorisnika
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<Korisnik> Korisniks { get; set; } = new List<Korisnik>();
}
