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

builder.Services.AddDbContext<SpotlyContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddScoped(typeof(IRepository<>), typeof(Repository<>));

builder.Services.AddScoped<IZahtjevRepository, ZahtjevRepository>();
builder.Services.AddScoped<IKorisnikRepository, KorisnikRepository>();
builder.Services.AddScoped<IDokumentacijaRepository, DokumentacijaRepository>();
builder.Services.AddScoped<IKaznaRepository, KaznaRepository>();
builder.Services.AddScoped<ITipKorisnikaRepository, TipKorisnikaRepository>();

builder.Services.AddScoped<IZahtjevService, ZahtjevService>();
builder.Services.AddScoped<IKorisnikService, KorisnikService>();
builder.Services.AddScoped<IDokumentacijaService, DokumentacijaService>();
builder.Services.AddScoped<IKaznaService, KaznaService>();

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
