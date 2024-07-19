package org.unibl.etf.mapa;

public class Mapa {

    // mozda bi trebalo da bude SINGLETON pattern, ali za sada neka ostane ovako

    private static final int VELICINA_MAPE = 20;
    public static PoljeNaMapi[][] mapa = new PoljeNaMapi[VELICINA_MAPE][VELICINA_MAPE];

    public Mapa() {
        for(int i = 0; i < VELICINA_MAPE; i++) {
            for(int j = 0; j < VELICINA_MAPE; j++) {
                mapa[i][j]  = new PoljeNaMapi(i, j);
            }
        }
    }


}
