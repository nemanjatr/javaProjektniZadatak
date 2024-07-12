package org.unibl.etf.vozila;

public class PrevoznoSredstvo {
    protected String jedinstveniIdentifikator;
    protected double cijenaNabavke;
    protected String proizvodjac;
    protected String model;
    protected int trenutniNivoBaterije;

    public PrevoznoSredstvo(String jedinstveniIdentifikator, double cijenaNabavke, String proizvodjac, String model, int trenutniNivoBaterije) {
        this.jedinstveniIdentifikator = jedinstveniIdentifikator;
        this.cijenaNabavke = cijenaNabavke;
        this.proizvodjac = proizvodjac;
        this.model = model;
        this.trenutniNivoBaterije = trenutniNivoBaterije;
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
}
