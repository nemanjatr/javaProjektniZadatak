package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Kvar {

    private String vrstaPrevoznogSredstva;
    private String idPrevoznogSredstva;
    private LocalDateTime datumVrijemeKvara;
    private String opisKvara;

    public Kvar(String vrstaPrevoznogSredstva, String idPrevoznogSredstva, LocalDateTime datumVrijemeKvara, String opisKvara){
        this.vrstaPrevoznogSredstva = vrstaPrevoznogSredstva;
        this.idPrevoznogSredstva = idPrevoznogSredstva;
        this.datumVrijemeKvara = datumVrijemeKvara;
        this.opisKvara = opisKvara;
    }

    public String getVrstaPrevoznogSredstva() {
        return vrstaPrevoznogSredstva;
    }

    public String getIdPrevoznogSredstva() {
        return idPrevoznogSredstva;
    }

    public LocalDateTime getDatumVrijemeKvara() {
        return datumVrijemeKvara;
    }

    public String getOpisKvara() {
        return opisKvara;
    }
}
