package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.mapa.PoljeNaMapi;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Iznajmljivanje {
    private LocalDateTime  datumVrijeme;
    private String imeKorisnika;
    private PoljeNaMapi pocetnaLokacija;
    private PoljeNaMapi krajnjaLokacija;
    private int trajanjeVoznjeSekunde;
    private boolean desioSeKvar;
    private boolean imaPromociju;

    //private Racun racunZaPlacanje;


    public Iznajmljivanje(String datumVrijeme, String imeKorisnika, String pocetnaLokacija, String krajnjaLokacija,
                          String trajanjeVoznjeSekunde, String desioSeKvar, String imaPromociju){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy H:mm");
        this.datumVrijeme = LocalDateTime.parse(datumVrijeme.trim(), formatter);
        this.imeKorisnika = imeKorisnika;

        //

        String[] parsiranaPocetnaLokacija = pocetnaLokacija.split(",");
        this.pocetnaLokacija = new PoljeNaMapi(Integer.parseInt(parsiranaPocetnaLokacija[0]),
                Integer.parseInt(parsiranaPocetnaLokacija[1]));
        String[] parsiranaKrajnjaLokacija = krajnjaLokacija.split(",");
        this.krajnjaLokacija = new PoljeNaMapi(Integer.parseInt(parsiranaKrajnjaLokacija[0]),
                Integer.parseInt(parsiranaKrajnjaLokacija[1]));

        //

        this.trajanjeVoznjeSekunde = Integer.parseInt(trajanjeVoznjeSekunde);
        this.desioSeKvar = desioSeKvar.equalsIgnoreCase("da");
        this.imaPromociju = imaPromociju.equalsIgnoreCase("da");
    }

    @Override
    public String toString(){
        return "datum i vrijeme " + datumVrijeme + ", " + "ime korisnika " + imeKorisnika + ", " +
                "pocetna Lokacija " + pocetnaLokacija + ", " + "krajnja lokacija " + krajnjaLokacija + ", " +
                "trajanje voznje u sek " + trajanjeVoznjeSekunde + ", " + "desio se kvar " + desioSeKvar + ", " +
                "ima promociju " + imaPromociju;

    }

}
