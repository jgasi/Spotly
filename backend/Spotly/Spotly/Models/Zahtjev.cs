using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Zahtjev
{
    public int Id { get; set; }

    public string Predmet { get; set; } = null!;

    public string Poruka { get; set; } = null!;

    public string? Odgovor { get; set; }

    public string? Status { get; set; }

    public byte[] DatumVrijeme { get; set; } = null!;

    public int? AdminId { get; set; }

    public int? KorisnikId { get; set; }

    public int? TipZahtjevaId { get; set; }

    public virtual ICollection<Dokumentacija> Dokumentacijas { get; set; } = new List<Dokumentacija>();

    public virtual Korisnik? Korisnik { get; set; }

    public virtual TipZahtjeva? TipZahtjeva { get; set; }
}
