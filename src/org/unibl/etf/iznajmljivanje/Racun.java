package org.unibl.etf.iznajmljivanje;

public class Racun {
    private double osnovnaCijena;
    private double iznos;
    private double ukupnoZaPlacanje;
    private double iznosPopusta;
    private double iznosPromocije;

    public Racun(){

    }

    public Racun(int osnovnaCijena, int ukupnoZaPlacanje){
        this.osnovnaCijena = osnovnaCijena;
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public double getOsnovnaCijena() {
        return osnovnaCijena;
    }

    public void setOsnovnaCijena(double osnovnaCijena) {
        this.osnovnaCijena = osnovnaCijena;
    }

    public double getUkupnoZaPlacanje() {
        return ukupnoZaPlacanje;
    }

    public void setUkupnoZaPlacanje(double ukupnoZaPlacanje) {
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public double getIznosPopusta() {
        return iznosPopusta;
    }

    public void setIznosPopusta(double iznosPopusta) {
        this.iznosPopusta = iznosPopusta;
    }

    public double getIznosPromocije() {
        return iznosPromocije;
    }

    public void setIznosPromocije(double iznosPromocije) {
        this.iznosPromocije = iznosPromocije;
    }


    @Override
    public String toString() {
        return "Racun: " + "\n\tosnovna cijena: " + osnovnaCijena + ", iznos: " + iznos + " ukupno za placanje: " + ukupnoZaPlacanje;
    }
}