package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ElektricniAutomobil extends PrevoznoSredstvo {

    private Date datumNabavke;
    private String opis;

    public ElektricniAutomobil(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model,
                               String datumNabavke, String opis) throws PogresniUlazniPodaciException {

        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);
        SimpleDateFormat formatDatuma = new SimpleDateFormat("d.M.yyyy.");
        try {
                this.datumNabavke = formatDatuma.parse(datumNabavke);
        } catch (ParseException e) {
            System.out.println("Greska pri parsiranju datuma nabavke vozila " + jedinstveniIdentifikator);
            throw new PogresniUlazniPodaciException();
        }
        if(opis.isEmpty()) {
            throw  new PogresniUlazniPodaciException();
        }
        this.opis = opis;
    }

    public Date getDatumNabavke() {
        return datumNabavke;
    }

    public String getOpis() {
        return opis;
    }
}
