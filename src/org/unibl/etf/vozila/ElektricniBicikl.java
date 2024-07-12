package org.unibl.etf.vozila;

public class ElektricniBicikl extends PrevoznoSredstvo {
    private int domet;

    public ElektricniBicikl(String jedinstveniIdentifikator, double cijenaNabavke, String proizvodjac, String model, int trenutniNivoBaterije, int domet) {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
        this.domet = domet;
    }
}
