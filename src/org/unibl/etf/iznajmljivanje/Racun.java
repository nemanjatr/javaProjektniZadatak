package org.unibl.etf.iznajmljivanje;


/**
 * Class Racun represents receipt of the rental.
 * It has several fields that are calculated on different way depending
 * on the rental characteristics
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class Racun {

    /**
     * Basic price of the rental based on the type of the vehicle (value in
     * parametri.properties file) and the duration of the rental.
     */
    private double osnovnaCijena;

    /**
     * The amount of the Racun object, based on multiple of osnovnaCijena
     * and distance between starting and end location of the rental.
     */
    private double iznos;

    /**
     * Total sum of the rental that is calculated by extracting value of
     * iznosPopusta and iznosPromocije from iznos field.
     */
    private double ukupnoZaPlacanje;

    /**
     * Value of the discount that is calculated with
     * multiplication of iznos and discount percentage
     * defined in parametri.properties file.
    * Every 10th iznajmljivanje gets discount.
     */
    private double iznosPopusta;

    /**
     * Value of the promotion that is calculated with
     * multiplication of iznos and discount percentage
     * defined in parametri.properties file.
     */
    private double iznosPromocije;


    private String tarifaNaplacivanje;

    /**
     * Constructor without parameters.
     */
    public Racun(){

    }

    /**
     * Constructor that sets base price and total sum
     * @param osnovnaCijena Base price.
     * @param ukupnoZaPlacanje Total sum.
     */
    public Racun(int osnovnaCijena, int ukupnoZaPlacanje){
        this.osnovnaCijena = osnovnaCijena;
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }


    /* Getters */

    public double getOsnovnaCijena() {
        return osnovnaCijena;
    }

    public void setOsnovnaCijena(double osnovnaCijena) {
        this.osnovnaCijena = osnovnaCijena;
    }

    public double getUkupnoZaPlacanje() {
        return ukupnoZaPlacanje;
    }

    public void setUkupnoZaPlacanje(double ukupnoZaPlacanje) {
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public double getIznosPopusta() {
        return iznosPopusta;
    }

    public void setIznosPopusta(double iznosPopusta) {
        this.iznosPopusta = iznosPopusta;
    }

    public double getIznosPromocije() {
        return iznosPromocije;
    }

    public void setIznosPromocije(double iznosPromocije) {
        this.iznosPromocije = iznosPromocije;
    }

    public void setTarifaNaplacivanje(String tarifaNaplacivanje) {
        this.tarifaNaplacivanje = tarifaNaplacivanje;
    }

    public String getTarifaNaplacivanja() {
        return tarifaNaplacivanje;
    }


    /**
     * Redefitinion of toString()
     * @return String representation of object.
     */
    @Override
    public String toString() {
        return "Racun: " + "\n\tosnovna cijena: " + osnovnaCijena + ", iznos: " + iznos + " ukupno za placanje: " + ukupnoZaPlacanje;
    }
}