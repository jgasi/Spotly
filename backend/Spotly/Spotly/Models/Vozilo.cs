using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Vozilo
{
    public int Id { get; set; }

    public string Marka { get; set; } = null!;

    public string Model { get; set; } = null!;

    public string? Godiste { get; set; }

    public string Registracija { get; set; } = null!;

    public int? TipVozilaId { get; set; }

    public int? KorisnikId { get; set; }

    public virtual Korisnik? Korisnik { get; set; }

    public virtual ICollection<Rezervacija> Rezervacijas { get; set; } = new List<Rezervacija>();

    public virtual TipVozila? TipVozila { get; set; }
}
