package org.unibl.etf.izuzeci;

public class PotrosenaBaterijeException extends Exception {
    public PotrosenaBaterijeException() {
        System.out.println("Baterija vozila je potrosena. Prekida se kretanje!");
    }
}
