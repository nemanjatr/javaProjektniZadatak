package org.unibl.etf.korisnik;

/**
 * Class for ID document of the user. It abstracts any possible
 * user ID document.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class IdentifikacioniDokument {

    /**
     * Name of the document (e.g. passport)
     */
    private String nazivDokumenta;
    /**
     * Unique number of the document.
     */
    private String brojDokumenta;

    /**
     * Constructor.
     * @param nazivDokumenta Name of the document.
     * @param brojDokumenta Uniqueu number of the document.
     */
    public IdentifikacioniDokument(String nazivDokumenta, String brojDokumenta) {
        this.nazivDokumenta = nazivDokumenta;
        this.brojDokumenta = brojDokumenta;
    }

    /**
     * Getter for the document name.
     * @return String representing document name.
     */
    public String getNazivDokumenta() {
        return nazivDokumenta;
    }

    /**
     * Getter for the document number
     * @return String representing number of the document.
     */
    public String getBrojDokumenta() {
        return brojDokumenta;
    }

    /**
     * Redefinition of the toString method.
     * @return String representation of the IdentifikacioniDokument object.
     */
    @Override
    public String toString() {
        return " dokument: " + this.nazivDokumenta + ", broj: " + this.brojDokumenta;
    }
}
