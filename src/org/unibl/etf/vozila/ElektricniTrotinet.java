package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

public class ElektricniTrotinet extends PrevoznoSredstvo {
    private int maksimalnaBrzina;

    public ElektricniTrotinet(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model, String maksimalnaBrzina) throws PogresniUlazniPodaciException {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);

        try {
            this.maksimalnaBrzina = Integer.parseInt(maksimalnaBrzina);
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException();
        }
    }

    public int getMaksimalnaBrzina() {
        return maksimalnaBrzina;
    }


}
