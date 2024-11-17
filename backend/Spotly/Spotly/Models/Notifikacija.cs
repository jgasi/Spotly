using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Notifikacija
{
    public int Id { get; set; }

    public string Poruka { get; set; } = null!;

    public DateTime DatumVrijeme { get; set; }

    public int? TipNotifikacijeId { get; set; }

    public int? KorisnikId { get; set; }

    public virtual Korisnik? Korisnik { get; set; }

    public virtual TipNotifikacije? TipNotifikacije { get; set; }
}
