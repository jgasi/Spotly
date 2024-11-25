using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace Spotly.Models;

public partial class SpotlyContext : DbContext
{
    public SpotlyContext()
    {
    }

    public SpotlyContext(DbContextOptions<SpotlyContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Dokumentacija> Dokumentacijas { get; set; }

    public virtual DbSet<Kazna> Kaznas { get; set; }

    public virtual DbSet<Korisnik> Korisniks { get; set; }

    public virtual DbSet<Notifikacija> Notifikacijas { get; set; }

    public virtual DbSet<ParkingMjesto> ParkingMjestos { get; set; }

    public virtual DbSet<Rezervacija> Rezervacijas { get; set; }

    public virtual DbSet<TipKazne> TipKaznes { get; set; }

    public virtual DbSet<TipKorisnika> TipKorisnikas { get; set; }

    public virtual DbSet<TipMjestum> TipMjesta { get; set; }

    public virtual DbSet<TipNotifikacije> TipNotifikacijes { get; set; }

    public virtual DbSet<TipVozila> TipVozilas { get; set; }

    public virtual DbSet<TipZahtjeva> TipZahtjevas { get; set; }

    public virtual DbSet<Vozilo> Vozilos { get; set; }

    public virtual DbSet<Zahtjev> Zahtjevs { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see https://go.microsoft.com/fwlink/?LinkId=723263.
        => optionsBuilder.UseSqlServer("Server=DESKTOP-T03I3I5;Database=Spotly;Trusted_Connection=True;TrustServerCertificate=True");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Dokumentacija>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Dokument__3214EC27D9EBD3D3");

            entity.ToTable("Dokumentacija");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.KaznaId).HasColumnName("KaznaID");
            entity.Property(e => e.ZahtjevId).HasColumnName("ZahtjevID");

            entity.HasOne(d => d.Kazna).WithMany(p => p.Dokumentacijas)
                .HasForeignKey(d => d.KaznaId)
                .HasConstraintName("FK__Dokumenta__Kazna__5070F446");

            entity.HasOne(d => d.Zahtjev).WithMany(p => p.Dokumentacijas)
                .HasForeignKey(d => d.ZahtjevId)
                .HasConstraintName("FK__Dokumenta__Zahtj__5165187F");
        });

        modelBuilder.Entity<Kazna>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Kazna__3214EC2702F41E65");

            entity.ToTable("Kazna");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.AdminId).HasColumnName("Admin_ID");
            entity.Property(e => e.DatumVrijeme)
                .HasColumnType("datetime")
                .HasColumnName("Datum_vrijeme");
            entity.Property(e => e.KorisnikId).HasColumnName("KorisnikID");
            entity.Property(e => e.NovcaniIznos)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("Novcani_iznos");
            entity.Property(e => e.Razlog)
                .HasMaxLength(500)
                .IsUnicode(false);
            entity.Property(e => e.TipKazneId).HasColumnName("Tip_kazneID");

            entity.HasOne(d => d.Korisnik).WithMany(p => p.Kaznas)
                .HasForeignKey(d => d.KorisnikId)
                .HasConstraintName("FK__Kazna__KorisnikI__52593CB8");

            entity.HasOne(d => d.TipKazne).WithMany(p => p.Kaznas)
                .HasForeignKey(d => d.TipKazneId)
                .HasConstraintName("FK__Kazna__Tip_kazne__534D60F1");
        });

        modelBuilder.Entity<Korisnik>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Korisnik__3214EC2732ECB2AD");

            entity.ToTable("Korisnik");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.BrojMobitela)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("Broj_mobitela");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Ime)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Lozinka)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Prezime)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Status)
                .HasMaxLength(50)
                .IsUnicode(false);
            entity.Property(e => e.TipKorisnikaId).HasColumnName("Tip_korisnikaID");

            entity.HasOne(d => d.TipKorisnika).WithMany(p => p.Korisniks)
                .HasForeignKey(d => d.TipKorisnikaId)
                .HasConstraintName("FK__Korisnik__Tip_ko__5441852A");
        });

        modelBuilder.Entity<Notifikacija>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Notifika__3214EC27586691BF");

            entity.ToTable("Notifikacija");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.DatumVrijeme)
                .HasColumnType("datetime")
                .HasColumnName("Datum_vrijeme");
            entity.Property(e => e.KorisnikId).HasColumnName("KorisnikID");
            entity.Property(e => e.Poruka)
                .HasMaxLength(500)
                .IsUnicode(false);
            entity.Property(e => e.TipNotifikacijeId).HasColumnName("Tip_notifikacijeID");

            entity.HasOne(d => d.Korisnik).WithMany(p => p.Notifikacijas)
                .HasForeignKey(d => d.KorisnikId)
                .HasConstraintName("FK__Notifikac__Koris__5535A963");

            entity.HasOne(d => d.TipNotifikacije).WithMany(p => p.Notifikacijas)
                .HasForeignKey(d => d.TipNotifikacijeId)
                .HasConstraintName("FK__Notifikac__Tip_n__5629CD9C");
        });

        modelBuilder.Entity<ParkingMjesto>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Parking___3214EC27399AD32B");

            entity.ToTable("Parking_mjesto");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Dostupnost)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Status)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.TipMjestaId).HasColumnName("Tip_mjestaID");

            entity.HasOne(d => d.TipMjesta).WithMany(p => p.ParkingMjestos)
                .HasForeignKey(d => d.TipMjestaId)
                .HasConstraintName("FK__Parking_m__Tip_m__571DF1D5");
        });

        modelBuilder.Entity<Rezervacija>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Rezervac__3214EC279360B9C1");

            entity.ToTable("Rezervacija");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.DatumVrijemeOdlaska)
                .HasColumnType("datetime")
                .HasColumnName("Datum_vrijeme_odlaska");
            entity.Property(e => e.DatumVrijemeRezervacije)
                .HasColumnType("datetime")
                .HasColumnName("Datum_vrijeme_rezervacije");
            entity.Property(e => e.ParkingMjestoId).HasColumnName("Parking_mjestoID");
            entity.Property(e => e.VoziloId).HasColumnName("VoziloID");

            entity.HasOne(d => d.ParkingMjesto).WithMany(p => p.Rezervacijas)
                .HasForeignKey(d => d.ParkingMjestoId)
                .HasConstraintName("FK__Rezervaci__Parki__5812160E");

            entity.HasOne(d => d.Vozilo).WithMany(p => p.Rezervacijas)
                .HasForeignKey(d => d.VoziloId)
                .HasConstraintName("FK__Rezervaci__Vozil__59063A47");
        });

        modelBuilder.Entity<TipKazne>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_kazn__3214EC2780A62654");

            entity.ToTable("Tip_kazne");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<TipKorisnika>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_kori__3214EC270C3FC06B");

            entity.ToTable("Tip_korisnika");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<TipMjestum>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_mjes__3214EC2733FB39F8");

            entity.ToTable("Tip_mjesta");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<TipNotifikacije>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_noti__3214EC27477A28D0");

            entity.ToTable("Tip_notifikacije");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<TipVozila>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_vozi__3214EC276FC50BAD");

            entity.ToTable("Tip_vozila");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<TipZahtjeva>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Tip_zaht__3214EC274C895181");

            entity.ToTable("Tip_zahtjeva");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Tip)
                .HasMaxLength(100)
                .IsUnicode(false);
        });

        modelBuilder.Entity<Vozilo>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Vozilo__3214EC27EEC446EE");

            entity.ToTable("Vozilo");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Godiste)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.KorisnikId).HasColumnName("KorisnikID");
            entity.Property(e => e.Marka)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Model)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Registracija)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.TipVozilaId).HasColumnName("Tip_vozilaID");

            entity.HasOne(d => d.Korisnik).WithMany(p => p.Vozilos)
                .HasForeignKey(d => d.KorisnikId)
                .HasConstraintName("FK__Vozilo__Korisnik__59FA5E80");

            entity.HasOne(d => d.TipVozila).WithMany(p => p.Vozilos)
                .HasForeignKey(d => d.TipVozilaId)
                .HasConstraintName("FK__Vozilo__Tip_vozi__5AEE82B9");
        });

        modelBuilder.Entity<Zahtjev>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Zahtjev__3214EC27AF9E9D83");

            entity.ToTable("Zahtjev");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.AdminId).HasColumnName("Admin_ID");
            entity.Property(e => e.DatumVrijeme)
                .HasColumnType("datetime")
                .HasColumnName("Datum_vrijeme");
            entity.Property(e => e.KorisnikId).HasColumnName("KorisnikID");
            entity.Property(e => e.Odgovor)
                .HasMaxLength(500)
                .IsUnicode(false);
            entity.Property(e => e.Poruka)
                .HasMaxLength(500)
                .IsUnicode(false);
            entity.Property(e => e.Predmet)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.Status)
                .HasMaxLength(100)
                .IsUnicode(false);
            entity.Property(e => e.TipZahtjevaId).HasColumnName("Tip_zahtjevaID");

            entity.HasOne(d => d.Korisnik).WithMany(p => p.Zahtjevs)
                .HasForeignKey(d => d.KorisnikId)
                .HasConstraintName("FK__Zahtjev__Korisni__5BE2A6F2");

            entity.HasOne(d => d.TipZahtjeva).WithMany(p => p.Zahtjevs)
                .HasForeignKey(d => d.TipZahtjevaId)
                .HasConstraintName("FK__Zahtjev__Tip_zah__5CD6CB2B");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
