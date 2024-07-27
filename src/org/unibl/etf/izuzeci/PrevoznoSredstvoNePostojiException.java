package org.unibl.etf.izuzeci;

public class PrevoznoSredstvoNePostojiException extends  Exception {

    public PrevoznoSredstvoNePostojiException(String exceptionPoruka) {
        System.out.println(exceptionPoruka);
    }

    public PrevoznoSredstvoNePostojiException() {
        System.out.println("Prevozno sredstvo nije moguce iznajmiti, jer ne postoji.");
    }

}
