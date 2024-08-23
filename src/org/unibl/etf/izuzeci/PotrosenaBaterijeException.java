package org.unibl.etf.izuzeci;

/**
 * Class the extends Exception class, and is a user-defined checked exception
 * that is used when vehicle is moving on the map, and it runs out of battery.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class PotrosenaBaterijeException extends Exception {

    /**
     * Constructor without parameters, prints out default message
     */
    public PotrosenaBaterijeException() {
        System.out.println("Baterija vozila je potrosena. Prekida se kretanje!");
    }
}
