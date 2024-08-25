package org.unibl.etf.iznajmljivanje;

import java.time.LocalDateTime;

/**
 * Class Kvar that abstracts failure that can happen when rental is being
 * executed.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class Kvar {

    /**
     * Type of the vehicle on which the failure happened.
     */
    private String vrstaPrevoznogSredstva;
    /**
     * ID of the vehicle on which the failure happened.
     */
    private String idPrevoznogSredstva;

    /**
     * Date and Time of the failure.
     */
    private LocalDateTime datumVrijemeKvara;

    /**
     * Short description of the failure.
     */
    private String opisKvara;

    /**
     * Constructor that sets all fields of the object.
     * @param vrstaPrevoznogSredstva Type of the vehicle on which the failure happened.
     * @param idPrevoznogSredstva ID of the vehicle on which the failure happened.
     * @param datumVrijemeKvara Date and Time of the failure.
     * @param opisKvara Short description of the failure.
     */
    public Kvar(String vrstaPrevoznogSredstva, String idPrevoznogSredstva, LocalDateTime datumVrijemeKvara, String opisKvara){
        this.vrstaPrevoznogSredstva = vrstaPrevoznogSredstva;
        this.idPrevoznogSredstva = idPrevoznogSredstva;
        this.datumVrijemeKvara = datumVrijemeKvara;
        this.opisKvara = opisKvara;
    }

    /**
     * Getter for the type of the vehicle.
     * @return String value of the vehicle type.
     */
    public String getVrstaPrevoznogSredstva() {
        return vrstaPrevoznogSredstva;
    }

    /**
     * Getter for the ID of the vehicle.
     * @return String value of the vehicle ID.
     */
    public String getIdPrevoznogSredstva() {
        return idPrevoznogSredstva;
    }

    /**
     * Getter for the Date and Time of the failure.
     * @return LocalDateTime object represetingn date and time when failure happened.
     */
    public LocalDateTime getDatumVrijemeKvara() {
        return datumVrijemeKvara;
    }

    /**
     * Getter fot the failure description.
     * @return String containing description of the failure.
     */
    public String getOpisKvara() {
        return opisKvara;
    }
}
