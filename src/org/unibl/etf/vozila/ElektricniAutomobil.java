package org.unibl.etf.vozila;

public class ElektricniAutomobil extends PrevoznoSredstvo {

    private String datumNabavke;
    private String opis;

    public ElektricniAutomobil(String jedinstveniIdentifikator, double cijenaNabavke, String proizvodjac,
                               String model, int trenutniNivoBaterije, String datumNabavke, String opis) {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
        this.datumNabavke = datumNabavke;
        this.opis = opis;
    }
}
