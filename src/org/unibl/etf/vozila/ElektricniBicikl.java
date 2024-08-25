package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

/**
 * ElektricniBicikl class represents a bicycle object, as a subtype of some vehicle.
 * It adds some specific attribute(s) to the general vehicle object.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class ElektricniBicikl extends PrevoznoSredstvo {

    /**
     * Exception message in case a user defined exception is thrown, when input data is wrong.
     */
    private static final String EXCEPTION_PORUKA = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan domet vozila ";

    /**
     * Specific attribute of the bicycle object, with the range of driving value.
     */
    private int domet;

    /**
     * Constructor that sets all the parent class fields, so it calls super constructor first, and then
     * assigns its own specific field.
     *
     * @param jedinstveniIdentifikator Unique identifier of the object.
     * @param cijenaNabavke Price of acquisition a vehicle.
     * @param proizvodjac Manufacturer of the vehicle.
     * @param model Specific model of the vehicle.
     * @param domet Attribute with range value.
     *
     * @throws PogresniUlazniPodaciException Exception thrown when input data is wrong.
     */
    public ElektricniBicikl(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model, String domet) throws PogresniUlazniPodaciException {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);
        try {
            this.domet = Integer.parseInt(domet);
        } catch (NumberFormatException e) {
            throw  new PogresniUlazniPodaciException(EXCEPTION_PORUKA + jedinstveniIdentifikator);

        }

    }

    /**
     * Getter for the range attribute.
     *
     * @return Int value of the range.
     */
    public int getDomet() {
        return domet;
    }
}
