package org.unibl.etf.korisnik;

public class Korisnik {
    String imeKorisnika;
    private String brojLicneKarte;
    private String brojPasosa;
    private String brojVozackeDozvole;
    private boolean imaVozackuDozvolu;
    private boolean jesteStraniDrzavljanin;

    public Korisnik(String imeKorisnika) {
        this.imeKorisnika = imeKorisnika;

    }

    public String getImeKorisnika() {
        return imeKorisnika;
    }

    @Override
    public String toString() {
        String stringZaIspis = "";
        String idDokument = (jesteStraniDrzavljanin) ? "pasos: " + brojPasosa + " (strani drzavljanin)" : "licna karta: " + brojLicneKarte;
        String vozackaDozvola = (imaVozackuDozvolu) ? "vozacka dozvola" + brojVozackeDozvole : "nema vozacku dozvolu";

        return "Korisnik " + imeKorisnika + " " + idDokument + vozackaDozvola;
    }

}
