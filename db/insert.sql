-- Insert data into Tip_korisnika
INSERT INTO Tip_korisnika (Tip) VALUES ('Zaposlenik');
INSERT INTO Tip_korisnika (Tip) VALUES ('Običan');
INSERT INTO Tip_korisnika (Tip) VALUES ('Admin');

-- Insert data into Tip_kazne
INSERT INTO Tip_kazne (Tip) VALUES ('Nepravilno parkiranje');
INSERT INTO Tip_kazne (Tip) VALUES ('Parkiranje na blokirano mjesto');

-- Insert data into Tip_zahtjeva
INSERT INTO Tip_zahtjeva (Tip) VALUES ('Zahtjev za registraciju');
INSERT INTO Tip_zahtjeva (Tip) VALUES ('Zahtjev za uklanjanje kazne');
INSERT INTO Tip_zahtjeva (Tip) VALUES ('Zahtjev za izmjenu profila korisnika');