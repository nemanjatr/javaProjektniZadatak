package org.unibl.etf.mapa;

public class Mapa {

    // mozda bi trebalo da bude SINGLETON pattern, ali za sada neka ostane ovako

    public static final int VELICINA_MAPE = 20;
    public static final int UZI_DIO_MAPE_DONJA_GRANICA = 5;
    public static final int UZI_DIO_MAPE_GORNJA_GRANICA = 14;

    public static PoljeNaMapi[][] mapa = new PoljeNaMapi[VELICINA_MAPE][VELICINA_MAPE];

    public Mapa() {
        for(int i = 0; i < VELICINA_MAPE; i++) {
            for(int j = 0; j < VELICINA_MAPE; j++) {
                mapa[i][j]  = new PoljeNaMapi(i, j);
            }
        }
    }


}
