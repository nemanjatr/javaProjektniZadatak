package org.unibl.etf.mapa;

public class PoljeNaMapi {
    private int koordinataX;
    private int koordinataY;

    public PoljeNaMapi() {

    }

    public PoljeNaMapi(int koordinataX, int koordinataY) {
        this.koordinataX = koordinataX;
        this.koordinataY = koordinataY;
    }

    public int getKoordinataY() {
        return koordinataY;
    }

    public int getKoordinataX() {
        return koordinataX;
    }

    public void setKoordinataX(int koordinataX) {
        this.koordinataX = koordinataX;
    }

    public void setKoordinataY(int koordinataY) {
        this.koordinataY = koordinataY;
    }

    public boolean unutarUzegDijelaGrada() {
       if((koordinataX >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && koordinataX <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA) &&
               (koordinataY >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && koordinataY <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA)) {
           return true;
       }
       return false;
    }

    public boolean unutarDozvoljenihGranica() {
        if((koordinataX >= 0 && koordinataX <= Mapa.VELICINA_MAPE - 1) && (koordinataY >= 0 && koordinataY <= Mapa.VELICINA_MAPE - 1)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + koordinataX + ", " + koordinataY + ")";
    }

    @Override
    public boolean equals(Object obj) {
        PoljeNaMapi polje = (PoljeNaMapi)obj;
        return(this.getKoordinataX() == polje.getKoordinataX() && this.getKoordinataY() == polje.getKoordinataY());
    }
}

