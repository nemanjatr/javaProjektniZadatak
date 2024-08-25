package org.unibl.etf.korisnik;

import java.util.Random;

/**
 * Class that abstracts user of the EMobility Company services.
 * It has a fields with user's personal information.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 20244
 */
public class Korisnik {

    /**
     * Name of the user.
     */
    private String imeKorisnika;
    /**
     * Field that represents user's ID document.
     * It is a separate class, also in this package.
     */
    private IdentifikacioniDokument dokument;
    /**
     * Field that represents user's citizenship.
     * It is a separate class object, also in this package.
     */
    private Drzavljanstvo drzavljanstvo;
    /**
     * String that represents user's driver license number
     * if he has one, if not the its null object.
     */
    private String brojVozackeDozvole = null;

    /**
     * Constructor with one parameter.
     * @param imeKorisnika Name of the user.
     */
    public Korisnik(String imeKorisnika) {
        this.imeKorisnika = imeKorisnika;

        /* Random generator to randomization of the decision whether
        * user is native of foreign citizen,
        * and also to generate some value for its documents
        */
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

    /**
     * Getter for citizenship field.
     * @return Citizenship drzavljanstvo field.
     */
    public Drzavljanstvo getDrzavljanstvo() {
        return drzavljanstvo;
    }

    /**
     * Getter for ID document field.
     * @return ID Document dokument field.
     */
    public IdentifikacioniDokument getDokument() {
        return dokument;
    }

    /**
     * Getter for the number of driver license.
     * @return License number field.
     */
    public String getBrojVozackeDozvole() {
        return brojVozackeDozvole;
    }


    /**
     * Getter for the user's name.
     * @return String with user's name.
     */
    public String getImeKorisnika() {
        return imeKorisnika;
    }

    /**
     * String representation of the Korisnik object.
     * Containing all relevant fields in a console friendly print-out.
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        String drzavljanstvo =Drzavljanstvo.STRANO.equals(this.drzavljanstvo) ? " strani drzavljanin" : " domaci drzavljanin";
        String idDokument = this.dokument.toString();
        String vozackaDozvola = (brojVozackeDozvole != null) ? " vozacka dozvola broj: " + brojVozackeDozvole : " nema vozacku dozvolu";

        return "Korisnik " + imeKorisnika + "\n" + drzavljanstvo + "\n" +  idDokument + " \n" + vozackaDozvola;
    }

}
