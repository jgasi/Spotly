using Microsoft.EntityFrameworkCore;
using Spotly.Data.Repositories;
using Spotly.Models;
using Spotly.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var dbPath = Path.Combine("/Users/toniivanovic/Desktop/air/Spotly/db", "spotly.db");
builder.Services.AddDbContext<SpotlyContext>(options =>
    //options.UseSqlite($"Data Source={dbPath}"));
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddScoped(typeof(IRepository<>), typeof(Repository<>));

builder.Services.AddScoped<IZahtjevRepository, ZahtjevRepository>();
builder.Services.AddScoped<IKorisnikRepository, KorisnikRepository>();
builder.Services.AddScoped<IDokumentacijaRepository, DokumentacijaRepository>();
builder.Services.AddScoped<IKaznaRepository, KaznaRepository>();
builder.Services.AddScoped<ITipKorisnikaRepository, TipKorisnikaRepository>();
builder.Services.AddScoped<IVoziloRepository, VoziloRepository>();
builder.Services.AddScoped<ITipVozilaRepository, TipVozilaRepository>();
builder.Services.AddScoped<IParkingMjestoRepository, ParkingMjestoRepository>();
builder.Services.AddScoped<IRezervacijaRepository, RezervacijaRepository>();

builder.Services.AddScoped<IZahtjevService, ZahtjevService>();
builder.Services.AddScoped<IKorisnikService, KorisnikService>();
builder.Services.AddScoped<IDokumentacijaService, DokumentacijaService>();
builder.Services.AddScoped<IKaznaService, KaznaService>();
builder.Services.AddScoped<IVoziloService, VoziloService>();
builder.Services.AddScoped<ITipVozilaService, TipVozilaService>();
builder.Services.AddScoped<IParkingMjestoService, ParkingMjestoService>();
builder.Services.AddScoped<IRezervacijaService, RezervacijaService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
