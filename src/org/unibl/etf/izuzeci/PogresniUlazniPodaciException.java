package org.unibl.etf.izuzeci;

public class PogresniUlazniPodaciException extends Exception {

    public PogresniUlazniPodaciException() {
        System.out.println("Greska pri ucitavanju ulaznih podataka iz fajla!");
    }

    public PogresniUlazniPodaciException(String msg) {
        System.out.println(msg);
    }
}
