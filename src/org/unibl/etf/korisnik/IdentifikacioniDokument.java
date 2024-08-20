package org.unibl.etf.korisnik;

public class IdentifikacioniDokument {

    private String nazivDokumenta;
    private String brojDokumenta;

    public IdentifikacioniDokument(String nazivDokumenta, String brojDokumenta) {
        this.nazivDokumenta = nazivDokumenta;
        this.brojDokumenta = brojDokumenta;
    }

    public String getNazivDokumenta() {
        return nazivDokumenta;
    }

    public String getBrojDokumenta() {
        return brojDokumenta;
    }

    @Override
    public String toString() {
        return " dokument: " + this.nazivDokumenta + ", broj: " + this.brojDokumenta;
    }
}
