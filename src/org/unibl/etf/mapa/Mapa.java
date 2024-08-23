package org.unibl.etf.mapa;

/**
 * Class that contains model of Map - a matrix like object which simulates
 * a polygon for vehicles to move.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class Mapa {


    /**
     * Static final field that contains the dimensions of the map.
     */
    public static final int VELICINA_MAPE = 20;

    /**
     * The map is separated into two parts, one part of the map is referenced
     * as narrow part, and these constants define its borders.
     * <ul>
     *   <li>{@code UZI_DIO_MAPE_DONJA_GRANICA}: The lower boundary of the narrower map section.</li>
     *   <li>{@code UZI_DIO_MAPE_GORNJA_GRANICA}: The upper boundary of the narrower map section.</li>
     * </ul>
     */
    public static final int UZI_DIO_MAPE_DONJA_GRANICA = 5;
    public static final int UZI_DIO_MAPE_GORNJA_GRANICA = 14;

    /**
     * Static field that represents the map itself as a matrix of PoljeNaMapi
     * class objects
     */
    public static PoljeNaMapi[][] mapa = new PoljeNaMapi[VELICINA_MAPE][VELICINA_MAPE];

    /**
     * Constructor of the Mapa class. It is used for initialization of the Map.
     */
    public Mapa() {
        for(int i = 0; i < VELICINA_MAPE; i++) {
            for(int j = 0; j < VELICINA_MAPE; j++) {
                mapa[i][j]  = new PoljeNaMapi(i, j);
            }
        }
    }


}
