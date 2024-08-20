package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ElektricniAutomobil extends PrevoznoSredstvo {

    private static final String EXCEPTION_PORUKA_DATUM = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan datum nabavke vozila ";
    private static final String EXCEPTION_PORUKA_OPIS = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan opis vozila ";

    private LocalDate datumNabavke;
    private String opis;
    private ArrayList<String> spisakPutnika = new ArrayList<>();

    public ElektricniAutomobil(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model,
                               String datumNabavke, String opis) throws PogresniUlazniPodaciException {

        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("d.M.yyyy.");
        try {
            this.datumNabavke = LocalDate.parse(datumNabavke, formater);
        } catch (DateTimeException e) {
            throw new PogresniUlazniPodaciException(EXCEPTION_PORUKA_DATUM + jedinstveniIdentifikator);
        }
        if(opis.isEmpty()) {
            throw  new PogresniUlazniPodaciException(EXCEPTION_PORUKA_OPIS + jedinstveniIdentifikator);
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
