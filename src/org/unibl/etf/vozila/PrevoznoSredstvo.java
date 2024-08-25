package org.unibl.etf.vozila;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;
import org.unibl.etf.izuzeci.PotrosenaBaterijeException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract class PrevoznoSredstvo, top class in hierarchy of vehicle type objects.
 * It contains common attributes for all vehicle objects, hence all of them are
 * declared with protected access modifier. It contains getters for all needed
 * fields, and methods for managing trenutniNivoBaterije field, regarding batter
 * charge-drain function.
 * <p>
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public abstract class PrevoznoSredstvo implements Serializable {


    /**
     * Unique identifier of object.
     */
    protected String jedinstveniIdentifikator;
    /**
     * Price attached to each vehicle object.
     */
    protected double cijenaNabavke;
    /**
     * Manufacturer of each vehicle object.
     */
    protected String proizvodjac;
    /**
     * Specific model of each vehicle object.
     * Each manufacturer has several vehicle models.
     */
    protected String model;
    /**
     * Current battery level, it changes during the simulation
     * using methods declared in this class.
     */
    protected int trenutniNivoBaterije;

    /**
     * Battery level at the start of the simulation, it is
     * declared as a constant, so it can be easily modified
     * if needed.
     */
    protected static int POCETNI_NIVO_BATERIJE = 100;
    /**
     * Maximum battery level, also a constatn so, if needed,
     * can be changed.
     */
    protected static int MAKSIMALNI_NIVO_BATERIJE = 100;


    /**
     * Constructor of the class, with all the attributes that are relevant in the moment of
     * creating vehicle object. That is, all the values taken from the passed .csv file with
     * data about the vehicle.
     *
     * @param jedinstveniIdentifikator Unique identifier of the object.
     * @param cijenaNabavke Price of acquisition a vehicle.
     * @param proizvodjac Manufacturer of the vehicle.
     * @param model Specific model of the vehicle.
     */
    public PrevoznoSredstvo(String jedinstveniIdentifikator, String cijenaNabavke, String proizvodjac, String model) {

        this.jedinstveniIdentifikator = jedinstveniIdentifikator;
        this.proizvodjac = proizvodjac;
        this.model = model;
        this.trenutniNivoBaterije = POCETNI_NIVO_BATERIJE;
        this.cijenaNabavke = Double.parseDouble(cijenaNabavke);

    }

    /**
     * Getter method for manufacturer value.
     * @return String with manufacturer of the vehicle.
     */
    public String getProizvodjac() {
        return proizvodjac;
    }

    /**
     * Getter method for unique identifier value.
     * @return String with unique identifier of the vehicle.
     */
    public String getJedinstveniIdentifikator() {
        return jedinstveniIdentifikator;
    }

    /**
     * Getter method for price of acquisition value.
     * @return Double value with acquisition price.
     */
    public double getCijenaNabavke() {
        return cijenaNabavke;
    }

    /**
     * Getter method for price of model.
     * @return String with model of the vehicle.
     */
    public String getModel() {
        return model;
    }

    /**
     * Getter method for current battery level.
     * @return Int value with current battery level.
     */
    public int getTrenutniNivoBaterije() {
        return trenutniNivoBaterije;
    }

    /**
     * Method for reducing battery level for 1, only if battery level is not empty,
     * that is if it is greater than zero.
     *
     * @throws PotrosenaBaterijeException In case battery is empty, user defined exception happens.
     */
    public void umanjiNivoBaterije() throws PotrosenaBaterijeException {
        if (trenutniNivoBaterije > 0) {
            this.trenutniNivoBaterije--;
        } else {
            throw new PotrosenaBaterijeException();
        }
    }

    /**
     * Method for recharging the battery to some value, passed as an argument.
     *
     * @param dopunjenaVrijednost Int value with value of charging level.
     */
    public void dopuniBateriju(int dopunjenaVrijednost) {
        if(this.trenutniNivoBaterije + dopunjenaVrijednost > MAKSIMALNI_NIVO_BATERIJE) {
            this.trenutniNivoBaterije = MAKSIMALNI_NIVO_BATERIJE;
        } else {
            this.trenutniNivoBaterije += dopunjenaVrijednost;
        }
    }

    /**
     * Redefined toString method from Object class.
     *
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return "Vozilo " + jedinstveniIdentifikator;
    }

    /**
     * Redefined equals method, that checks if two vehicle objects are identical, based
     * on their unique id value.
     *
     * @param obj Some object to be compared with current (this) object.
     * @return Boolean with true value if object are identical or false, if ther are not.
     */
    @Override
    public boolean equals(Object obj) {
        PrevoznoSredstvo ps = (PrevoznoSredstvo) obj;
        return Objects.equals(this.jedinstveniIdentifikator, ps.jedinstveniIdentifikator);
    }

    /**
     * Method that generates hash value of the object based on its unique id.
     *
     * @return Int value that represents hash value of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.jedinstveniIdentifikator);
    }

}
