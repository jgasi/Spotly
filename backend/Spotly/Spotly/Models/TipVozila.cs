using System.Text.Json.Serialization;

namespace Spotly.Models;

public partial class TipVozila
{
    public int Id { get; set; }

    public string Tip { get; set; } = null!;

    [JsonIgnore]
    public virtual ICollection<Vozilo> Vozilos { get; set; } = new List<Vozilo>();
}
