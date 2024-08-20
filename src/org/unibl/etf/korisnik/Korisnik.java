package org.unibl.etf.korisnik;

import java.util.Random;

public class Korisnik {
    private String imeKorisnika;
    private IdentifikacioniDokument dokument;
    private Drzavljanstvo drzavljanstvo;
    private String brojVozackeDozvole = null;

    public Korisnik(String imeKorisnika) {
        this.imeKorisnika = imeKorisnika;

        Random generator = new Random();
        if(generator.nextInt(10) < 2) {
            this.drzavljanstvo = Drzavljanstvo.STRANO;
            this.dokument = new IdentifikacioniDokument("pasos", String.valueOf(generator.nextInt(10000)));
        } else {
            this.drzavljanstvo = Drzavljanstvo.DOMACE;
            this.dokument = new IdentifikacioniDokument("licna karta", String.valueOf(generator.nextInt(10000)));
        }

        if(generator.nextInt(10) < 8) {
            this.brojVozackeDozvole = String.valueOf(generator.nextInt(10000));
        }

    }


    public Drzavljanstvo getDrzavljanstvo() {
        return drzavljanstvo;
    }

    public IdentifikacioniDokument getDokument() {
        return dokument;
    }

    public String getBrojVozackeDozvole() {
        return brojVozackeDozvole;
    }


    public String getImeKorisnika() {
        return imeKorisnika;
    }

    @Override
    public String toString() {
        String stringZaIspis = "";
        String drzavljanstvo = this.drzavljanstvo.equals(Drzavljanstvo.STRANO) ? " strani drzavljanin" : " domaci drzavljanin";
        String idDokument = this.dokument.toString();
        String vozackaDozvola = (brojVozackeDozvole != null) ? " vozacka dozvola broj: " + brojVozackeDozvole : " nema vozacku dozvolu";

        return "Korisnik " + imeKorisnika + "\n" + drzavljanstvo + "\n" +  idDokument + " \n" + vozackaDozvola;
    }

}
