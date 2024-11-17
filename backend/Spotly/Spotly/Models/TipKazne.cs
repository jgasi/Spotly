using System;
using System.Collections.Generic;

namespace Spotly.Models;

public partial class TipKazne
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    public virtual ICollection<Kazna> Kaznas { get; set; } = new List<Kazna>();
}
