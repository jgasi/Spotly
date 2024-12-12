using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace Spotly.Models;

public partial class Korisnik
{
    public int Id { get; set; }

    public string Ime { get; set; } = null!;

    public string Prezime { get; set; } = null!;

    public string Email { get; set; } = null!;

    public string Lozinka { get; set; } = null!;

    public string? BrojMobitela { get; set; }

    public string? Status { get; set; }

    public int? TipKorisnikaId { get; set; }

    public virtual ICollection<Kazna> Kaznas { get; set; } = new List<Kazna>();

    public virtual ICollection<Notifikacija> Notifikacijas { get; set; } = new List<Notifikacija>();

    public virtual TipKorisnika? TipKorisnika { get; set; }

    [JsonIgnore]
    public virtual ICollection<Vozilo> Vozilos { get; set; } = new List<Vozilo>();

    public virtual ICollection<Zahtjev> Zahtjevs { get; set; } = new List<Zahtjev>();
}
