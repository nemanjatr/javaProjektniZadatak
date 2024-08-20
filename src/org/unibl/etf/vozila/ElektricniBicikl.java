package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

public class ElektricniBicikl extends PrevoznoSredstvo {

    private static final String EXCEPTION_PORUKA = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan domet vozila ";

    private int domet;

    public ElektricniBicikl(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model, String domet) throws PogresniUlazniPodaciException {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);
        try {
            this.domet = Integer.parseInt(domet);
        } catch (NumberFormatException e) {
            throw  new PogresniUlazniPodaciException(EXCEPTION_PORUKA + jedinstveniIdentifikator);

        }

    }

    public int getDomet() {
        return domet;
    }
}
