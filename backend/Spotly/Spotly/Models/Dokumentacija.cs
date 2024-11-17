using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class Dokumentacija
{
    public int Id { get; set; }

    public byte[]? Slika { get; set; }

    public int? KaznaId { get; set; }

    public int? ZahtjevId { get; set; }

    public virtual Kazna? Kazna { get; set; }

    public virtual Zahtjev? Zahtjev { get; set; }
}
