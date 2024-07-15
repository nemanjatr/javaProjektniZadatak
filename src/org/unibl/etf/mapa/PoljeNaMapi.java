package org.unibl.etf.mapa;

public class PoljeNaMapi {
    private int koordinataX;
    private int koordinataY;

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
       if((koordinataX >= 5 && koordinataX <= 14) && (koordinataY >= 5 && koordinataY <= 14)) {
           return true;
       }
       return false;
    }

    @Override
    public String toString() {
        return "(" + koordinataX + ", " + koordinataY + ")";
    }
}

