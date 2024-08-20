package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

public class ElektricniTrotinet extends PrevoznoSredstvo {
    public static final String EXCEPTION_PORUKA = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivana maks brzina vozila ";
    private int maksimalnaBrzina;

    public ElektricniTrotinet(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model, String maksimalnaBrzina) throws PogresniUlazniPodaciException {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);

        try {
            this.maksimalnaBrzina = Integer.parseInt(maksimalnaBrzina);
        } catch (NumberFormatException e) {
            throw  new PogresniUlazniPodaciException(EXCEPTION_PORUKA + jedinstveniIdentifikator);

        }
    }

    public int getMaksimalnaBrzina() {
        return maksimalnaBrzina;
    }


}
