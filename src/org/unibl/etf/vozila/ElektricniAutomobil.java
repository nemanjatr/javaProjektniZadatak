package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ElektricniAutomobil extends PrevoznoSredstvo {

    private LocalDate datumNabavke;
    private String opis;

    public ElektricniAutomobil(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model,
                               String datumNabavke, String opis) throws PogresniUlazniPodaciException {

        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("d.M.yyyy.");
        try {
            this.datumNabavke = LocalDate.parse(datumNabavke, formater);
        } catch (DateTimeException e) {
            System.out.println("Greska pri parsiranju datuma nabavke vozila " + jedinstveniIdentifikator);
            throw new PogresniUlazniPodaciException();
        }
        if(opis.isEmpty()) {
            throw  new PogresniUlazniPodaciException();
        }
        this.opis = opis;
    }

    public LocalDate getDatumNabavke() {
        return datumNabavke;
    }

    public String getOpis() {
        return opis;
    }
}
