package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ElektricniAutomobil class represents a car object, as a subtype of some vehicle.
 * It adds some specific attribute(s) to the general vehicle object.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class ElektricniAutomobil extends PrevoznoSredstvo {

    /**
     * Exception message in case of the wrong date input field..
     */
    private static final String EXCEPTION_PORUKA_DATUM = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan datum nabavke vozila ";
    /**
     * Exception message in case of the wrong description field input.
     */
    private static final String EXCEPTION_PORUKA_OPIS = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan opis vozila ";

    /**
     * Field representing date of acquisition for the car.
     */
    private LocalDate datumNabavke;
    /**
     * Field representing description of the car.
     */
    private String opis;

    /**
     * Field representing all passengers in the car, put in a List, where each
     * passenger is described with its name.
     */
    private ArrayList<String> spisakPutnika = new ArrayList<>();


    /**
     * Constructor that sets all the parent class fields, so it calls super constructor first, and then
     * assigns its own specific field.
     *
     * @param jedinstveniIdentifikator Unique identifier of the object.
     * @param cijenaNabavke Price of acquisition a vehicle.
     * @param proizvodjac Manufacturer of the vehicle.
     * @param model Specific model of the vehicle.
     * @param datumNabavke Attribute of the car regarding date of acquisition/
     * @param opis Attribute of the car regarding its description.
     *
     * @throws PogresniUlazniPodaciException Exception thrown when input data is wrong.
     */
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

    /**
     * Getter method for date of acquisition.
     *
     * @return LocalDate value with date of acquisition.
     */
    public LocalDate getDatumNabavke() {
        return datumNabavke;
    }

    /**
     * Getter method for description field.
     *
     * @return String with description.
     */
    public String getOpis() {
        return opis;
    }
}
