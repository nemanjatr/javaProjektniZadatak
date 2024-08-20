package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;
import org.unibl.etf.izuzeci.PotrosenaBaterijeException;

import java.io.Serializable;
import java.util.Objects;

public class PrevoznoSredstvo implements Serializable {
    protected String jedinstveniIdentifikator;
    protected double cijenaNabavke;
    protected String proizvodjac;
    protected String model;
    protected int trenutniNivoBaterije;
    protected static int POCETNI_NIVO_BATERIJE = 100;


    public PrevoznoSredstvo(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model) throws PogresniUlazniPodaciException {

        this.jedinstveniIdentifikator = jedinstveniIdentifikator;
        this.proizvodjac = proizvodjac;
        this.model = model;
        this.trenutniNivoBaterije = POCETNI_NIVO_BATERIJE;
        this.cijenaNabavke = Double.parseDouble(cijenaNabavke);

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

    public void umanjiNivoBaterije() throws PotrosenaBaterijeException {
        if (trenutniNivoBaterije > 0) {
            this.trenutniNivoBaterije--;
        } else {
            throw new PotrosenaBaterijeException();
        }
    }

    @Override
    public String toString() {
        return "Vozilo " + jedinstveniIdentifikator;
    }

    @Override
    public boolean equals(Object obj) {
        PrevoznoSredstvo ps = (PrevoznoSredstvo) obj;
        return Objects.equals(this.jedinstveniIdentifikator, ps.jedinstveniIdentifikator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jedinstveniIdentifikator);
    }

}
