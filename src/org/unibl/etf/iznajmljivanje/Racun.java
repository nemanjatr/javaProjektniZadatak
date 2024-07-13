package org.unibl.etf.iznajmljivanje;

public class Racun {
    private int osnovnaCijena;
    private int iznos;
    private int ukupnoZaPlacanje;

    public Racun(){

    }

    public Racun(int osnovnaCijena, int ukupnoZaPlacanje){
        this.osnovnaCijena = osnovnaCijena;
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public int getOsnovnaCijena() {
        return osnovnaCijena;
    }

    public void setOsnovnaCijena(int osnovnaCijena) {
        this.osnovnaCijena = osnovnaCijena;
    }

    public int getUkupnoZaPlacanje() {
        return ukupnoZaPlacanje;
    }

    public void setUkupnoZaPlacanje(int ukupnoZaPlacanje) {
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }
}
