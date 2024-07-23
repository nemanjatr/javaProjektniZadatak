package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.mapa.Mapa;
import org.unibl.etf.mapa.PoljeNaMapi;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Iznajmljivanje extends Thread {

    public static final int DISTANCE_NARROW = 5;
    public static final int DISTANCE_WIDE = 10;
    public static final double DISCOUNT = 0.1;
    public static final double DISCOUT_PROM = 0.2;
    public static final int CAR_UNIT_PRICE = 3;
    public static final int BIKE_UNIT_PRICE = 2;
    public static final int SCOOTER_UNIT_PRICE = 1;

    private static int brojacInstanci = 0;
    private int redniBrojIznajmljivanja;

    private LocalDateTime  datumVrijeme;
    private String imeKorisnika;

    // obratiti paznju jer ovo polje nije navedeno u tekstu zadatka, ali ja mislim da je neophodno
    private String identifikatorPrevoznogSredstva;

    private PoljeNaMapi pocetnaLokacija;
    private PoljeNaMapi krajnjaLokacija;
    private int trajanjeVoznjeSekunde;
    private boolean desioSeKvar;
    private boolean imaPromociju;

    private String tarifaNaplacivanja;
    private Racun racunZaPlacanje;


    private static Object lock = new Object();





    public Iznajmljivanje(String datumVrijeme, String imeKorisnika, String identifikatorPrevoznogSredstva, String pocetnaLokacija,
                          String krajnjaLokacija, String trajanjeVoznjeSekunde, String desioSeKvar, String imaPromociju){

        brojacInstanci++;
        this.redniBrojIznajmljivanja = brojacInstanci;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy H:mm");
        this.datumVrijeme = LocalDateTime.parse(datumVrijeme.trim(), formatter);
        this.imeKorisnika = imeKorisnika;

        //
        this.identifikatorPrevoznogSredstva = identifikatorPrevoznogSredstva;
        //

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
        this.racunZaPlacanje = new Racun();
    }

    public LocalDateTime getDatumVrijeme() {
        return datumVrijeme;
    }



    public void racunanjeOsnovneCijene() {
        if('A' == identifikatorPrevoznogSredstva.charAt(0)) {
            racunZaPlacanje.setOsnovnaCijena((int)CAR_UNIT_PRICE * trajanjeVoznjeSekunde);
        } else if('B' == identifikatorPrevoznogSredstva.charAt(0)) {
            racunZaPlacanje.setOsnovnaCijena((int)BIKE_UNIT_PRICE * trajanjeVoznjeSekunde);
        } else if('T' == identifikatorPrevoznogSredstva.charAt(0)) {
            racunZaPlacanje.setOsnovnaCijena((int)SCOOTER_UNIT_PRICE * trajanjeVoznjeSekunde);
        } else {
            System.out.println("Pogresan jedinstveni identifikator vozila u podacima za iznajmljivanje");
        }
    }

    public void racunanjeTarifeNaplacivanja(){
        if(pocetnaLokacija.unutarUzegDijelaGrada()) {
            tarifaNaplacivanja = "uzi";
        } else {
            tarifaNaplacivanja = "siri";
        }
    }

    public void racunanjeIznosa() {
        int osnovnaCijena = racunZaPlacanje.getOsnovnaCijena();
        if("uzi".equals(tarifaNaplacivanja)) {
            racunZaPlacanje.setIznos(DISTANCE_NARROW * osnovnaCijena);
        } else if("siri".equals(tarifaNaplacivanja)) {
            racunZaPlacanje.setIznos(DISTANCE_WIDE * osnovnaCijena);
        } else {
            System.out.println("Greska u vrijednosti tarife naplacivanja. Ne odgovara ni tarifi za uzi dio grada niti za siri!");
        }
    }

    public void racunanjeUkupnogPlacanja() {
        int iznos = racunZaPlacanje.getIznos();
        int iznosPromocije = 0;
        int iznosPopusta = 0;
        if(desioSeKvar) {
            racunZaPlacanje.setUkupnoZaPlacanje(0);
        } else {
            if(redniBrojIznajmljivanja % 10 == 0) {
                iznosPopusta = (int) (DISCOUNT * iznos);
            }
            if(imaPromociju) {
                iznosPromocije = (int) (DISCOUT_PROM * iznos);
            }
            racunZaPlacanje.setUkupnoZaPlacanje(iznos - iznosPopusta - iznosPromocije);
        }
    }

    public void generisiRacun(){
        this.racunanjeOsnovneCijene();
        this.racunanjeTarifeNaplacivanja();
        this.racunanjeIznosa();
        this.racunanjeUkupnogPlacanja();
        System.out.println("Racun za " + redniBrojIznajmljivanja + " iznajmljivanje: " + "\t\n" +
                "     * ukupno za placanje " + racunZaPlacanje.getUkupnoZaPlacanje());
    }

    @Override
    public String toString(){
        return "datum i vrijeme " + datumVrijeme.toLocalDate() + " " + datumVrijeme.toLocalTime() + ", " +
                "ime korisnika " + imeKorisnika + ", " + "ID prevoznog sredstva" + identifikatorPrevoznogSredstva + ", " +
                "pocetna Lokacija " + pocetnaLokacija + ", " + "krajnja lokacija " + krajnjaLokacija + ", " +
                "trajanje voznje u sek " + trajanjeVoznjeSekunde + ", " + "desio se kvar " + desioSeKvar + ", " +
                "ima promociju " + imaPromociju;

    }

    @Override
    public void run() {

        int udaljenostKretanja = Math.abs(krajnjaLokacija.getKoordinataX() - pocetnaLokacija.getKoordinataX()) +
                Math.abs(krajnjaLokacija.getKoordinataY() - pocetnaLokacija.getKoordinataY());
        double trajanjeZadrzavanjaNaPoljuSekunde = (double) trajanjeVoznjeSekunde / udaljenostKretanja;


        System.out.println("Iznajmljivanje " + redniBrojIznajmljivanja + ", vozilo " + identifikatorPrevoznogSredstva);
        System.out.println("=> Vozilo " + identifikatorPrevoznogSredstva + " se nalazi na pocetnoj lokaciji " + pocetnaLokacija);
        PoljeNaMapi trenutnaLokacija = pocetnaLokacija;
        //pocetnaLokacija.setZauzeto();

        int i = pocetnaLokacija.getKoordinataX();
        int j = pocetnaLokacija.getKoordinataY();
        boolean naizmjenicno = true;
        boolean iDoKraja = false;
        boolean jdoKraja = false;

        int inkrementPozicijeX = 0;
        int inkrementPozicijeY = 0;

        if(pocetnaLokacija.getKoordinataX() < krajnjaLokacija.getKoordinataX()) {
            inkrementPozicijeX = 1;
        } else if(pocetnaLokacija.getKoordinataX() > krajnjaLokacija.getKoordinataX()) {
            inkrementPozicijeX = -1;
        }

        if(pocetnaLokacija.getKoordinataY() < krajnjaLokacija.getKoordinataY()) {
            inkrementPozicijeY = 1;
        } else if(pocetnaLokacija.getKoordinataY() > krajnjaLokacija.getKoordinataY()) {
            inkrementPozicijeY = -1;
        }


        while (trenutnaLokacija.getKoordinataX() !=  krajnjaLokacija.getKoordinataX()
                || trenutnaLokacija.getKoordinataY() != krajnjaLokacija.getKoordinataY()) {

            synchronized (lock) {

                if (naizmjenicno) {
                    i += inkrementPozicijeX;
                    if (i == krajnjaLokacija.getKoordinataX()) {
                        iDoKraja = true;
                        naizmjenicno = !naizmjenicno;
                    }
                } else {
                    j += inkrementPozicijeY;
                    if (j == krajnjaLokacija.getKoordinataY()) {
                        jdoKraja = true;
                        naizmjenicno = !naizmjenicno;
                    }
                }

                if (!iDoKraja && !jdoKraja) {
                    naizmjenicno = !naizmjenicno;
                }

                trenutnaLokacija = Mapa.mapa[i][j];
            }

            try {
                Thread.sleep((int) (trajanjeZadrzavanjaNaPoljuSekunde * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("  *** Vozilo " + identifikatorPrevoznogSredstva + " se nalazi na lokaciji " + trenutnaLokacija);
        }

        System.out.println("=> Vozilo " + identifikatorPrevoznogSredstva + " je stiglo na odrediste.");
    }

}
