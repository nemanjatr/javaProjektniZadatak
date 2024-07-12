package org.unibl.etf.vozila;

public class ElektricniTrotinet extends PrevoznoSredstvo {
    private int maksimalnaBrzina;

    public ElektricniTrotinet(String jedinstveniIdentifikator, double cijenaNabavke, String proizvodjac, String model, int trenutniNivoBaterije, int maksimalnaBrzina) {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
        this.maksimalnaBrzina = maksimalnaBrzina;
    }


}
