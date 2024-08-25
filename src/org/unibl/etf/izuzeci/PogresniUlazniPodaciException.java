package org.unibl.etf.izuzeci;

/**
 * Class the extends Exception class, and is a user-defined checked exception
 * that is used when data is loaded from the file, and some data are not as
 * expected.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class PogresniUlazniPodaciException extends Exception {

    /**
     * Class the extends Exception class, and is a user-defined checked exception
     * that is used when vehicle is moving on the map, and it runs out of battery.
     */
    public PogresniUlazniPodaciException() {
        System.out.println("Greska pri ucitavanju ulaznih podataka iz fajla!");
    }

    /**
     * Constructor with one String parameter, allowing programmer to set which
     * message to print when exception is thrown.
     * @param exceptionPoruka String message to be shown.
     */
    public PogresniUlazniPodaciException(String exceptionPoruka) {
        System.out.println(exceptionPoruka);
    }
}
