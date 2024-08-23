package org.unibl.etf.mapa;

/**
 * Class used for abstraction of one field on the Map object.
 * The map is a matrix of PoljeNaMapi fields.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class PoljeNaMapi {

    /**
     * The position of the PoljeNaMapi object on the Map,
     * regarding the x-axis.
     */
    private int koordinataX;
    /**
     * The position of the PoljeNaMapi object on the Map,
     * regarding the y-axis.
     */
    private int koordinataY;

    /**
     * Constructor of the class.
     * @param koordinataX Position regarding the x-axis.
     * @param koordinataY Position regarding the y-axis.
     */
    public PoljeNaMapi(int koordinataX, int koordinataY) {
        this.koordinataX = koordinataX;
        this.koordinataY = koordinataY;
    }

    /**
     * Getter of the koordinataX field.
     * @return Value of the koordinataX field
     */
    public int getKoordinataY() {
        return koordinataY;
    }

    /**
     * Getter of the koordinataY field.
     * @return Value of the koordinataY field
     */
    public int getKoordinataX() {
        return koordinataX;
    }


    /**
     * Checks whether current object is inside a narrow part of the Map.
     * @return Value true is returned if object is inside, and false if it
     * is outside the narrow part of the Map.
     */
    public boolean unutarUzegDijelaGrada() {
        return (koordinataX >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && koordinataX <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA) &&
                (koordinataY >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && koordinataY <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA);
    }

    /**
     * Checks whether current object is inside the borders of the Map.
     * @return Value true if it is inside the allowed borders, and
     * false if outside.
     */
    public boolean unutarDozvoljenihGranica() {
        return (koordinataX >= 0 && koordinataX <= Mapa.VELICINA_MAPE - 1) && (koordinataY >= 0 && koordinataY <= Mapa.VELICINA_MAPE - 1);
    }

    /**
     * Method represents object as a tuple of its corresponding coordinates,
     * using standard mathematical representation - (x, y)
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return "(" + koordinataX + ", " + koordinataY + ")";
    }

    /**
     * Checks whether two field objects are the same by comparing
     * its corresponding fields. Both fields should be equal, for
     * two object to be equal.
     * @param obj Object to be compared to the current object.
     * @return Value true is returned if the objects are equal, and
     * false if they are not.
     */
    @Override
    public boolean equals(Object obj) {
        PoljeNaMapi polje = (PoljeNaMapi)obj;
        return(this.getKoordinataX() == polje.getKoordinataX() && this.getKoordinataY() == polje.getKoordinataY());
    }
}

