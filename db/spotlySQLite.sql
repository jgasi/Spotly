-- Create database and tables
PRAGMA foreign_keys = ON;

CREATE TABLE Dokumentacija (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Slika BLOB,
    KaznaID INTEGER,
    ZahtjevID INTEGER,
    FOREIGN KEY (KaznaID) REFERENCES Kazna(ID),
    FOREIGN KEY (ZahtjevID) REFERENCES Zahtjev(ID)
);

CREATE TABLE Kazna (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Razlog TEXT NOT NULL,
    Novcani_iznos TEXT,
    Datum_vrijeme TEXT NOT NULL,
    Admin_ID INTEGER,
    KorisnikID INTEGER,
    Tip_kazneID INTEGER,
    FOREIGN KEY (KorisnikID) REFERENCES Korisnik(ID),
    FOREIGN KEY (Tip_kazneID) REFERENCES Tip_kazne(ID)
);

CREATE TABLE Korisnik (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Ime TEXT NOT NULL,
    Prezime TEXT NOT NULL,
    Email TEXT NOT NULL,
    Lozinka TEXT NOT NULL,
    Broj_mobitela TEXT,
    Status TEXT,
    Tip_korisnikaID INTEGER,
    FOREIGN KEY (Tip_korisnikaID) REFERENCES Tip_korisnika(ID)
);

CREATE TABLE Notifikacija (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Poruka TEXT NOT NULL,
    Datum_vrijeme TEXT NOT NULL,
    Tip_notifikacijeID INTEGER,
    KorisnikID INTEGER,
    FOREIGN KEY (KorisnikID) REFERENCES Korisnik(ID),
    FOREIGN KEY (Tip_notifikacijeID) REFERENCES Tip_notifikacije(ID)
);

CREATE TABLE Parking_mjesto (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Status TEXT NOT NULL,
    Dostupnost TEXT,
    Tip_mjestaID INTEGER,
    FOREIGN KEY (Tip_mjestaID) REFERENCES Tip_mjesta(ID)
);

CREATE TABLE Rezervacija (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Datum_vrijeme_rezervacije TEXT NOT NULL,
    Datum_vrijeme_odlaska TEXT,
    VoziloID INTEGER,
    Parking_mjestoID INTEGER,
    FOREIGN KEY (Parking_mjestoID) REFERENCES Parking_mjesto(ID),
    FOREIGN KEY (VoziloID) REFERENCES Vozilo(ID)
);

CREATE TABLE Tip_kazne (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Tip_korisnika (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Tip_mjesta (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Tip_notifikacije (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Tip_vozila (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Tip_zahtjeva (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Tip TEXT NOT NULL
);

CREATE TABLE Vozilo (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Marka TEXT NOT NULL,
    Model TEXT NOT NULL,
    Godiste TEXT,
    Registracija TEXT NOT NULL,
    Tip_vozilaID INTEGER,
    KorisnikID INTEGER,
    FOREIGN KEY (KorisnikID) REFERENCES Korisnik(ID),
    FOREIGN KEY (Tip_vozilaID) REFERENCES Tip_vozila(ID)
);

CREATE TABLE Zahtjev (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Predmet TEXT NOT NULL,
    Poruka TEXT NOT NULL,
    Odgovor TEXT,
    Status TEXT,
    Datum_vrijeme TEXT NOT NULL,
    Admin_ID INTEGER,
    KorisnikID INTEGER,
    Tip_zahtjevaID INTEGER,
    FOREIGN KEY (KorisnikID) REFERENCES Korisnik(ID),
    FOREIGN KEY (Tip_zahtjevaID) REFERENCES Tip_zahtjeva(ID)
);