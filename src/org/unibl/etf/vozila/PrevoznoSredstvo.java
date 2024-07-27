package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

public class PrevoznoSredstvo {
    protected String jedinstveniIdentifikator;
    protected double cijenaNabavke;
    protected String proizvodjac;
    protected String model;
    protected int trenutniNivoBaterije;

    public PrevoznoSredstvo(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model) throws PogresniUlazniPodaciException {

        if(jedinstveniIdentifikator.isEmpty() || proizvodjac.isEmpty() || model.isEmpty()) {
            throw new PogresniUlazniPodaciException();
        }
        this.jedinstveniIdentifikator = jedinstveniIdentifikator;
        this.proizvodjac = proizvodjac;
        this.model = model;
        this.trenutniNivoBaterije = 100;

        try {
            this.cijenaNabavke = Double.parseDouble(cijenaNabavke);
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException();
        }

    }

    public String getProizvodjac() {
        return proizvodjac;
    }

    public String getJedinstveniIdentifikator() {
        return jedinstveniIdentifikator;
    }

    public double getCijenaNabavke() {
        return cijenaNabavke;
    }

    public String getModel() {
        return model;
    }

    public int getTrenutniNivoBaterije() {
        return trenutniNivoBaterije;
    }

    @Override
    public String toString() {
        return jedinstveniIdentifikator;
    }
}
