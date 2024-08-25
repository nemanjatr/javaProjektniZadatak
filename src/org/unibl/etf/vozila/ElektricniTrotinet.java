package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

/**
 * ElektricniTrotinet class represents a scooter object, as a subtype of some vehicle.
 * It adds some specific attribute(s) to the general vehicle object.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */

public class ElektricniTrotinet extends PrevoznoSredstvo {

    /**
     * Message being passed when user defined exception happens, in case of wrong input data.
     */
    public static final String EXCEPTION_PORUKA = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivana maks brzina vozila ";

    /**
     * Attribute specific for the scooter object. It represents maximum speed this type of
     * vehicle can achieve.
     */
    private int maksimalnaBrzina;

    /**
     * Constructor that sets all the parent class fields, so it calls super constructor first, and then
     * assigns its own specific field.
     *
     * @param jedinstveniIdentifikator Unique identifier of the object.
     * @param cijenaNabavke Price of acquisition a vehicle.
     * @param proizvodjac Manufacturer of the vehicle.
     * @param model Specific model of the vehicle.
     * @param maksimalnaBrzina Maximum speed of the vehicle, attribute specific for the scooter.
     *
     * @throws PogresniUlazniPodaciException Exception thrown when input data is wrong.
     */
    public ElektricniTrotinet(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model, String maksimalnaBrzina) throws PogresniUlazniPodaciException {
        super(jedinstveniIdentifikator, cijenaNabavke, proizvodjac, model);

        try {
            this.maksimalnaBrzina = Integer.parseInt(maksimalnaBrzina);
        } catch (NumberFormatException e) {
            throw  new PogresniUlazniPodaciException(EXCEPTION_PORUKA + jedinstveniIdentifikator);

        }
    }

    /**
     * Getter for maximum speed field.
     *
     * @return Value of maximum speed as int.
     */
    public int getMaksimalnaBrzina() {
        return maksimalnaBrzina;
    }


}
