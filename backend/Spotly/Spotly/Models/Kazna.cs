using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Kazna
{
    public int Id { get; set; }

    public string Razlog { get; set; } = null!;

    public string? NovcaniIznos { get; set; }

    public DateTime DatumVrijeme { get; set; }

    public int? AdminId { get; set; }

    public int? KorisnikId { get; set; }

    public int? TipKazneId { get; set; }

    public virtual ICollection<Dokumentacija> Dokumentacijas { get; set; } = new List<Dokumentacija>();

    public virtual Korisnik? Korisnik { get; set; }

    public virtual TipKazne? TipKazne { get; set; }
}
