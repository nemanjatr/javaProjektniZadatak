package org.unibl.etf.izuzeci;

public class NedovoljnoUlaznihPodatakaException extends Exception {
    public NedovoljnoUlaznihPodatakaException(String exceptionPoruka) {
        System.out.println(exceptionPoruka);
    }

    public NedovoljnoUlaznihPodatakaException() {
        System.out.println("U ulaznom fajlu nedostaju podaci!");
    }
}
