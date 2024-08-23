package org.unibl.etf.izuzeci;

/**
 * Class the extends Exception class, and is a user-defined checked exception
 * that is used when executing rent, to check whether vehicle needed for the rent
 * is existing in the company. If not this exception is thrown.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class PrevoznoSredstvoNePostojiException extends  Exception {

    /**
     * Constructor with one String parameter, allowing programmer to set which
     * message to print when exception is thrown.
     * @param exceptionPoruka String message to be shown.
     */
    public PrevoznoSredstvoNePostojiException(String exceptionPoruka) {
        System.out.println(exceptionPoruka);
    }

    /**
     * Constructor without parameters, prints out default message.
     */
    public PrevoznoSredstvoNePostojiException() {
        System.out.println("Prevozno sredstvo nije moguce iznajmiti, jer ne postoji.");
    }

}
