package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;
import org.unibl.etf.izuzeci.PotrosenaBaterijeException;
import org.unibl.etf.korisnik.Korisnik;
import org.unibl.etf.mapa.Mapa;
import org.unibl.etf.mapa.PoljeNaMapi;
import org.unibl.etf.simulacija.Simulacija;
import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;

import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Iznajmljivanje extends Thread {

    private static final String PROPERTIES_PODACI_GRESKA = "Pogresni podaci unutar properties fajla!";
    private static final String PROPERTIES_UPOZORENJE = "Rezultati su mozda pogresni. Molimo provjerite vrijednosti unutar properties fajla!";
    private static final Properties properties;


    private static int brojacIznajmljivanja = 0;
    private int redniBrojIznajmljivanja;

    private LocalDateTime  datumVrijeme;
    private Korisnik korisnik;

    // obratiti paznju jer ovo polje nije navedeno u tekstu zadatka, ali ja mislim da je neophodno
    //private String identifikatorPrevoznogSredstva;
    private PrevoznoSredstvo prevoznoSredstvo;

    private PoljeNaMapi pocetnaLokacija;
    private PoljeNaMapi krajnjaLokacija;
    private int trajanjeVoznjeSekunde;
    private boolean desioSeKvar;
    private boolean imaPromociju;
    private boolean imaPopust;


    private String tarifaNaplacivanja;
    private Racun racunZaPlacanje;

    private static final Object lock = new Object();
    private static final Object lockPutanjaRacuna = new Object();


    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("parametri.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File folderRacuna = new File(properties.get("RACUNI_PUTANJA").toString());
        if(!folderRacuna.exists()) {
            folderRacuna.mkdir();
        } else {
            obrisiFajloveUnutarDirektorijuma(folderRacuna);
        }

        File folderSerijalizacije = new File(properties.get("SERIJALIZACIJA_PUTANJA").toString());
        if(!folderSerijalizacije.exists()) {
            folderSerijalizacije.mkdir();
        } else {
            obrisiFajloveUnutarDirektorijuma(folderSerijalizacije);
        }
    }

    public Iznajmljivanje(String datumVrijeme, String imeKorisnika, PrevoznoSredstvo prevoznoSredstvo, String pocetnaLokacija,
                          String krajnjaLokacija, String trajanjeVoznjeSekunde, String desioSeKvar, String imaPromociju) throws PogresniUlazniPodaciException {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy H:mm");
            this.datumVrijeme = LocalDateTime.parse(datumVrijeme.trim(), formatter);
            this.korisnik = new Korisnik(imeKorisnika);
            this.prevoznoSredstvo = prevoznoSredstvo;

            String[] parsiranaPocetnaLokacija = pocetnaLokacija.split(",");
            this.pocetnaLokacija = new PoljeNaMapi(Integer.parseInt(parsiranaPocetnaLokacija[0]),
                    Integer.parseInt(parsiranaPocetnaLokacija[1]));
            String[] parsiranaKrajnjaLokacija = krajnjaLokacija.split(",");
            this.krajnjaLokacija = new PoljeNaMapi(Integer.parseInt(parsiranaKrajnjaLokacija[0]),
                    Integer.parseInt(parsiranaKrajnjaLokacija[1]));

            if(!this.pocetnaLokacija.unutarDozvoljenihGranica() || !this.krajnjaLokacija.unutarDozvoljenihGranica()) {
                throw new PogresniUlazniPodaciException();
            }

            this.trajanjeVoznjeSekunde = Integer.parseInt(trajanjeVoznjeSekunde);
            this.desioSeKvar = desioSeKvar.equalsIgnoreCase("da");
            this.imaPromociju = imaPromociju.equalsIgnoreCase("da");

        } catch(NumberFormatException e) {
            throw new PogresniUlazniPodaciException();
        }

    }

    public LocalDateTime getDatumVrijeme() {
        return datumVrijeme;
    }

    public PrevoznoSredstvo getPrevoznoSredstvo() {
        return prevoznoSredstvo;
    }

    public Racun getRacunZaPlacanje() {
        return racunZaPlacanje;
    }

    public boolean isDesioSeKvar() {
        return desioSeKvar;
    }



    private void racunanjeOsnovneCijene() throws PogresniUlazniPodaciException{

        try {
            if(prevoznoSredstvo instanceof ElektricniAutomobil) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(properties.get("CAR_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else if(prevoznoSredstvo instanceof ElektricniBicikl) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(properties.get("BIKE_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else if(prevoznoSredstvo instanceof ElektricniTrotinet) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(properties.get("SCOOTER_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else {
                System.out.println("Pogresan jedinstveni identifikator vozila u podacima za iznajmljivanje");
            }

        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }

    }

    private void racunanjeTarifeNaplacivanja(){
        if(pocetnaLokacija.unutarUzegDijelaGrada() && krajnjaLokacija.unutarUzegDijelaGrada()) {
            tarifaNaplacivanja = "uzi";
        } else {
            tarifaNaplacivanja = "siri";
        }
    }

    private void racunanjeIznosa() throws PogresniUlazniPodaciException{
        double osnovnaCijena = racunZaPlacanje.getOsnovnaCijena();

        try {
            if("uzi".equals(tarifaNaplacivanja)) {
                racunZaPlacanje.setIznos(Double.parseDouble(properties.get("DISTANCE_NARROW").toString()) * osnovnaCijena);
            } else if("siri".equals(tarifaNaplacivanja)) {
                racunZaPlacanje.setIznos(Double.parseDouble(properties.get("DISTANCE_WIDE").toString()) * osnovnaCijena);
            } else {
                System.out.println("Greska u vrijednosti tarife naplacivanja. Ne odgovara tarifi niti za uzi dio grada niti za siri!");
            }
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }
    }

    private void racunanjeUkupnogPlacanja() throws PogresniUlazniPodaciException {
        double osnovnaCijena = racunZaPlacanje.getOsnovnaCijena();

        try {
            if(desioSeKvar) {
                racunZaPlacanje.setUkupnoZaPlacanje(0.0);
            } else {
                if(redniBrojIznajmljivanja % 10 == 0) {
                    racunZaPlacanje.setIznosPopusta(Double.parseDouble(properties.get("DISCOUNT").toString()) * osnovnaCijena);
                    imaPopust = true;
                }
                if(imaPromociju) {
                    racunZaPlacanje.setIznosPromocije(Double.parseDouble(properties.get("DISCOUNT_PROM").toString()) * osnovnaCijena);
                }
                racunZaPlacanje.setUkupnoZaPlacanje(racunZaPlacanje.getIznos() - racunZaPlacanje.getIznosPopusta() - racunZaPlacanje.getIznosPromocije());
            }
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }
    }

    public void generisiRacun() {
        this.racunZaPlacanje = new Racun();
        try {
            this.racunanjeOsnovneCijene();
            this.racunanjeTarifeNaplacivanja();
            this.racunanjeIznosa();
            this.racunanjeUkupnogPlacanja();
            System.out.println("Racun za " + redniBrojIznajmljivanja + " iznajmljivanje: " + "\t\n" +
                    "     * ukupno za placanje " + racunZaPlacanje.getUkupnoZaPlacanje());

        } catch (PogresniUlazniPodaciException e) {
            System.out.println(PROPERTIES_UPOZORENJE);
        }
    }

    public void upisiRacun() {

        String stringDatumVrijeme = datumVrijeme.toString().replace(":", "_");
        try(PrintWriter pisacFajlaRacuna = new PrintWriter(new BufferedWriter
                (new FileWriter(properties.get("RACUNI_PUTANJA") + stringDatumVrijeme + "_" +
                        korisnik.getImeKorisnika() + "_" + prevoznoSredstvo.getJedinstveniIdentifikator() + ".txt")))) {
            pisacFajlaRacuna.println(this);
            pisacFajlaRacuna.println(racunZaPlacanje);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void obrisiFajloveUnutarDirektorijuma(File putanjaDirektorijuma) {
        File[] files = putanjaDirektorijuma.listFiles();

        if (files != null && putanjaDirektorijuma.isDirectory()) {
            for (File fajl : files) {
                if (fajl.isDirectory()) {
                    obrisiFajloveUnutarDirektorijuma(fajl);
                }
                fajl.delete();
            }
        }
    }

    @Override
    public String toString(){
        String kvarIspis = (desioSeKvar) ? "desio se kvar" : "nije se desio kvar";
        String promocijaIspis = (imaPromociju) ? "ima promociju" : "nema promociju";
        String popustIspis = (imaPopust) ? "ima popust" : "nema popust";

        return "Iznajmljivanje: \n\t" + "datum i vrijeme: " + datumVrijeme.toLocalDate() + " " + datumVrijeme.toLocalTime() + ", " +
                "korisnik: " + korisnik.getImeKorisnika() +  ", " + "ID prevoznog sredstva: " + prevoznoSredstvo.getJedinstveniIdentifikator() + ",\n" +
                "\tpocetna Lokacija " + pocetnaLokacija + ", " + "krajnja lokacija " + krajnjaLokacija + ", " +
                "trajanje voznje u sek " + trajanjeVoznjeSekunde + ", " + kvarIspis + ", " + promocijaIspis + ", " + popustIspis
                + ", rb: " + redniBrojIznajmljivanja;

    }

    @Override
    public void run() {

        synchronized (lock) {
            brojacIznajmljivanja++;
            this.redniBrojIznajmljivanja = brojacIznajmljivanja;
        }

        int udaljenostKretanja = Math.abs(krajnjaLokacija.getKoordinataX() - pocetnaLokacija.getKoordinataX()) +
                Math.abs(krajnjaLokacija.getKoordinataY() - pocetnaLokacija.getKoordinataY());
        double trajanjeZadrzavanjaNaPoljuSekunde = (double) trajanjeVoznjeSekunde / udaljenostKretanja;


        //System.out.println("Iznajmljivanje " + redniBrojIznajmljivanja + ", vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator());
        System.out.println("=> Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " se nalazi na pocetnoj lokaciji " + pocetnaLokacija);

        // prikaz na pocetku
        PoljeNaMapi finalPocetnaLokacija = pocetnaLokacija;
        String prikazTeksaNaStartu = "<html>" + prevoznoSredstvo.getJedinstveniIdentifikator() + " - " +
                prevoznoSredstvo.getTrenutniNivoBaterije() + "<br>" + "START";
        //System.out.println(prikazTeksaNaStartu);
        SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                prikaziNaMapi(finalPocetnaLokacija, prikazTeksaNaStartu));
        try {
            Thread.sleep((int)(trajanjeZadrzavanjaNaPoljuSekunde * 1000)); // radi brzeg izvrsavanja
            //Thread.sleep((int) (trajanjeZadrzavanjaNaPoljuSekunde * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.ukloniSaMape(finalPocetnaLokacija));


        //


        PoljeNaMapi trenutnaLokacija = pocetnaLokacija;

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


//        while (trenutnaLokacija.getKoordinataX() !=  krajnjaLokacija.getKoordinataX()
//                || trenutnaLokacija.getKoordinataY() != krajnjaLokacija.getKoordinataY()) {
        while (!trenutnaLokacija.equals(krajnjaLokacija)) {

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

            PoljeNaMapi finalTrenutnaLokacija = trenutnaLokacija;
            String tekstZaIspis = "<html>" + prevoznoSredstvo.getJedinstveniIdentifikator() + " - " +  prevoznoSredstvo.getTrenutniNivoBaterije();
            if(finalTrenutnaLokacija.equals(krajnjaLokacija)) {
                tekstZaIspis += "<br>" + "KRAJ";

            }

            final String tekstZaLambu = tekstZaIspis;
            SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                    prikaziNaMapi(finalTrenutnaLokacija, tekstZaLambu));

            try {
                Thread.sleep((int)(trajanjeZadrzavanjaNaPoljuSekunde * 5000)); // radi brzeg izvrsavanja
                //Thread.sleep((int) (trajanjeZadrzavanjaNaPoljuSekunde * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                    ukloniSaMape(finalTrenutnaLokacija));
            try {
                prevoznoSredstvo.umanjiNivoBaterije();
            } catch (PotrosenaBaterijeException e) {
                System.out.println("Prevozno sredstvo " + prevoznoSredstvo + "  nije u mogucnosti da nastavi kretanje");
                return;
            }



            System.out.println("  *** Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " se nalazi na lokaciji " + trenutnaLokacija);
        }

        //System.out.println("=> Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " je stiglo na odrediste.");

        generisiRacun();
        synchronized (lockPutanjaRacuna) {
            upisiRacun();
        }
    }

}
