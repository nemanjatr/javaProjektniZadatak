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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Class that abstract one rent. It extends Thread, so it is a thread
 * object, and multiple rentals can be executed concurrently. It is essential class
 * for the simulation. All is being executed from the run() method as is usual for
 * threads. There the algorithm for moving is implemented, and at the end racun is
 * created for each Iznajmljivanje object.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class Iznajmljivanje extends Thread {

    /**
     * Constants that represents various print messages throughout the program.
     */
    private static final String PROPERTIES_PODACI_GRESKA = "Pogresni podaci unutar properties fajla!";
    private static final String PROPERTIES_UPOZORENJE = "Rezultati su mozda pogresni. Molimo provjerite vrijednosti unutar properties fajla!";
    private static final String EXCEPTION_PORUKA = "Greska pri ucitavanju ulaznih podataka iz fajla! Neocekivan datum iznajmljivanja!";
    private static final String RACUNANJE_CIJENE_PORUKA = "Pogresan jedinstveni identifikator vozila u podacima za iznajmljivanje";
    private static final String RACUNANJE_IZNOSA_PORUKA = "Greska u vrijednosti tarife naplacivanja. Ne odgovara tarifi niti za uzi dio grada niti za siri!";
    private static final String PARAMETRI_PROPERTIES = "Greska sa Properties fajlom parametri.properties";
    private static final String IZLAZNI_PROPERTIES = "Greska sa Properties fajlom outPath.properties!";
    private static final String ULAZNI_PROPERTIES = "Greska sa Properties fajlom inputPath.properties!";

    /**
     * String constants for comparing using equals method.
     */
    private static final String DA = "da";
    public static final String UZI = "uzi";
    public static final String SIRI = "siri";


    /**
     * Static properties objects, used for getting some parameters of the program
     * from the .properites files.
     * <ul>
     *      *   <li>{@code simProperties}: Contains data regarding calculation of the racun for iznajmljivanje</li>
     *      *   <li>{@code outPathProperties}: Contains data regarding paths of the output files (for racun's and serialization).</li>
     *      * <li>{@code inputPathProperties}: Contains paths of files with input data.</li>
     * </ul>
     */
    public static final Properties simProperties;
    public static final Properties outPathProperties;
    public static final Properties inputPathProperties;


    /**
     * Static field counting total number of rentals.
     */
    private static int brojacIznajmljivanja = 0;
    /**
     * Field containing ordinal number of each rental.
     */
    private int redniBrojIznajmljivanja;

    /**
     * Fields that represent the iznajmljivanje parameters.
     * <ul>
     *   <li>{@code datumVrijeme}: Date and Time of the rental.</li>
     *   <li>{@code korisnik}: User object of the rental.</li>
     *   <li>{@code prevoznoSredstvo}: Vehicle object of the rental.</li>
     *   <li>{@code pocetnaLokacija}: Starting point of the rental on the map.</li>
     *   <li>{@code krajnjaLokacija}: Ending point of the rental on the map.</li>
     *   <li>{@code trajanjeVoznjeSekunde}: Duration integer field of the rental in seconds.</li>
     *   <li>{@code desioSeKvar}: Field of type boolean that says whether failure happened or not.</li>
     *   <li>{@code imaPromociju}: Field of type boolean that says whether rental has promotion or not.</li>
     *   <li>{@code imaPopust}: Field of type boolean that says whether rental has discount or not.</li>
     * </ul>
     */
    private LocalDateTime  datumVrijeme;
    private Korisnik korisnik;
    private PrevoznoSredstvo prevoznoSredstvo;
    private PoljeNaMapi pocetnaLokacija;
    private PoljeNaMapi krajnjaLokacija;
    private int trajanjeVoznjeSekunde;
    private boolean desioSeKvar;
    private boolean imaPromociju;
    private boolean imaPopust;


    /**
     * String field that determines what how Racun object will be
     * calculated.
     */
    private String tarifaNaplacivanja;

    /**
     * Racun object, calculated for each rental object, and then stored into the file.
     */
    private Racun racunZaPlacanje;


    /**
     * Object type lock objects used for synchronization.
     */
    private static final Object lock = new Object();
    private static final Object lockPutanjaRacuna = new Object();

    /**
     * Static File field representing directory where all files corresponding
     * each Racun object are stored.
     */
    public static File folderRacuna;

    /**
     * Static File field representing directory where all files corresponding to
     * Iznajmljivanje objects serialization are stored.
     */
    public static File folderSerijalizacije;


    /*
     * Static initialization block used to initialize properties objects,
     * and to create and(or) clear folder in which Racun objects will be stored
     * and also the same thing for serialization folder.
     */
    static {

        File fajlParametri = new File("parametri.properties");
        if(!fajlParametri.exists()) {
            System.out.println(PARAMETRI_PROPERTIES);
            System.exit(1);
        }
        simProperties = new Properties();
        try {
            simProperties.load(new FileInputStream(fajlParametri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File fajlIzlaznePutanje = new File("outPath.properties");
        if(!fajlIzlaznePutanje.exists()) {
            System.out.println(IZLAZNI_PROPERTIES);
            System.exit(1);
        }
        outPathProperties = new Properties();
        try {
            outPathProperties.load(new FileInputStream(fajlIzlaznePutanje));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File fajlUlaznePutanje = new File("inputPath.properties");
        if(!fajlUlaznePutanje.exists()) {
            System.out.println(ULAZNI_PROPERTIES);
            System.exit(1);
        }
        inputPathProperties = new Properties();
        try {
            inputPathProperties.load(new FileInputStream(fajlUlaznePutanje));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        folderRacuna = new File(outPathProperties.get("RACUNI_PUTANJA").toString());
        if(!folderRacuna.exists()) {
            folderRacuna.mkdir();
        } else {
            obrisiFajloveUnutarDirektorijuma(folderRacuna);
        }

        folderSerijalizacije = new File(outPathProperties.get("SERIJALIZACIJA_PUTANJA").toString());
        if(!folderSerijalizacije.exists()) {
            folderSerijalizacije.mkdir();
        } else {
            obrisiFajloveUnutarDirektorijuma(folderSerijalizacije);
        }
    }


    /**
     * Constructor of the Iznajmljivanje class with arguments that initialize its main parameters.
     * @param datumVrijeme Date and Time of the rental.
     * @param imeKorisnika User object of the rental.
     * @param prevoznoSredstvo Vehicle object of the rental.
     * @param pocetnaLokacija Starting point of the rental on the map.
     * @param krajnjaLokacija Ending point of the rental on the map.
     * @param trajanjeVoznjeSekunde Duration integer field of the rental in seconds.
     * @param desioSeKvar Field of type boolean that says whether failure happened or not.
     * @param imaPromociju Field of type boolean that says whether rental has promotion or not.
     *
     * @throws PogresniUlazniPodaciException Exception is throwed if input data is not correct.
     */
    public Iznajmljivanje(String datumVrijeme, String imeKorisnika, PrevoznoSredstvo prevoznoSredstvo, String pocetnaLokacija,
                          String krajnjaLokacija, String trajanjeVoznjeSekunde, String desioSeKvar, String imaPromociju) throws PogresniUlazniPodaciException {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            this.datumVrijeme = LocalDateTime.parse(datumVrijeme.trim(), formatter);
        } catch (DateTimeException e) {
            throw new PogresniUlazniPodaciException(EXCEPTION_PORUKA);
        }

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
        this.desioSeKvar = DA.equalsIgnoreCase(desioSeKvar);
        this.imaPromociju = DA.equalsIgnoreCase(imaPromociju);
    }

    /* Getters of the needed field */

    public LocalDateTime getDatumVrijeme() {
        return datumVrijeme;
    }

    public LocalDate getDatum() {
        return datumVrijeme.toLocalDate();
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

    public Korisnik getKorisnik() {
        return korisnik;
    }


    /**
     * Private function that calculates basic price of each Iznajmljivanje object, and
     * based on the calculation sets appropriate attribute of the Racun object correlated
     * with this Iznajmljivanje object.
     *
     * @throws PogresniUlazniPodaciException Throws user defined exception if input data is not correct.
     */
    private void racunanjeOsnovneCijene() throws PogresniUlazniPodaciException{

        try {
            if(prevoznoSredstvo instanceof ElektricniAutomobil) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(simProperties.get("CAR_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else if(prevoznoSredstvo instanceof ElektricniBicikl) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(simProperties.get("BIKE_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else if(prevoznoSredstvo instanceof ElektricniTrotinet) {
                racunZaPlacanje.setOsnovnaCijena(Double.parseDouble(simProperties.get("SCOOTER_UNIT_PRICE").toString()) * trajanjeVoznjeSekunde);
            } else {
                System.out.println(RACUNANJE_CIJENE_PORUKA);
            }

        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }

    }


    /**
     * Function that set field of this class : tarifaNaplacivanje to one of two
     * values based on starting and ending point of the rental. If both start and end are
     * in the narrow part of the map, then tarifaNaplacivanje is set to value "uzi",
     * if not then it is set to "siri".
     */
    private void racunanjeTarifeNaplacivanja(){
        if(pocetnaLokacija.unutarUzegDijelaGrada() && krajnjaLokacija.unutarUzegDijelaGrada()) {
            this.racunZaPlacanje.setTarifaNaplacivanje(UZI);
        } else {
            this.racunZaPlacanje.setTarifaNaplacivanje(SIRI);
        }
    }


    /**
     * Function that calculates field of the Racun object racunZaPlacanje of this rental,
     * and based of the calculation, it updated racunZaPlacanje object.
     *
     * @throws PogresniUlazniPodaciException In case of a NumberFormatException because of the wrong input data
     *                                       this user-defined exception will be thrown.
     */
    private void racunanjeIznosa() throws PogresniUlazniPodaciException{
        double osnovnaCijena = racunZaPlacanje.getOsnovnaCijena();

        try {
            if(UZI.equals(this.racunZaPlacanje.getTarifaNaplacivanja())) {
                racunZaPlacanje.setIznos(Double.parseDouble(simProperties.get("DISTANCE_NARROW").toString()) * osnovnaCijena);
            } else if(SIRI.equals(this.racunZaPlacanje.getTarifaNaplacivanja())) {
                racunZaPlacanje.setIznos(Double.parseDouble(simProperties.get("DISTANCE_WIDE").toString()) * osnovnaCijena);
            } else {
                System.out.println(RACUNANJE_IZNOSA_PORUKA);
            }
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }
    }


    /**
     * Calculating final sum of the racunZaPlacanje object.
     *
     * @throws PogresniUlazniPodaciException In case of a NumberFormatException because of the wrong input data
     *                                       this user-defined exception will be thrown.
     */
    private void racunanjeUkupnogPlacanja() throws PogresniUlazniPodaciException {
        double osnovnaCijena = racunZaPlacanje.getOsnovnaCijena();

        try {
            if(desioSeKvar) {
                racunZaPlacanje.setUkupnoZaPlacanje(0.0);
            } else {
                if(redniBrojIznajmljivanja % 10 == 0) {
                    racunZaPlacanje.setIznosPopusta(Double.parseDouble(simProperties.get("DISCOUNT").toString()) * osnovnaCijena);
                    imaPopust = true;
                }
                if(imaPromociju) {
                    racunZaPlacanje.setIznosPromocije(Double.parseDouble(simProperties.get("DISCOUNT_PROM").toString()) * osnovnaCijena);
                }
                racunZaPlacanje.setUkupnoZaPlacanje(racunZaPlacanje.getIznos() - racunZaPlacanje.getIznosPopusta() - racunZaPlacanje.getIznosPromocije());
            }
        } catch (NumberFormatException e) {
            throw new PogresniUlazniPodaciException(PROPERTIES_PODACI_GRESKA);
        }
    }


    /**
     * Function that generates racunZaPlacanje by calling all utility
     * functions that calculate all needed values.
     */
    private void generisiRacun() {
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

    /**
     * Method that creates a file for this rental, and stores relevant data
     * regarding Racun object inside the file.
     */
    private void upisiRacun() {

        String stringDatumVrijeme = datumVrijeme.toString().replace(":", "_");
        try(PrintWriter pisacFajlaRacuna = new PrintWriter(new BufferedWriter
                (new FileWriter(Iznajmljivanje.folderRacuna + File.separator + stringDatumVrijeme + "_" +
                        korisnik.getImeKorisnika() + "_" + prevoznoSredstvo.getJedinstveniIdentifikator() + ".txt")))) {
            pisacFajlaRacuna.println(this);
            pisacFajlaRacuna.println(racunZaPlacanje);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method that deletes all files inside a given directory.
     *
     * @param putanjaDirektorijuma File object of directory in which files should be deleted.
     */
    private static void obrisiFajloveUnutarDirektorijuma(File putanjaDirektorijuma) {
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

    /**
     * Redefiniton of toString method.
     *
     * @return Returns string representation of Iznajmljivanje object.
     */
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


    /**
     * Redefinition of run method.
     */
    @Override
    public void run() {

        /* Since brojacIznajmljivanja is static variable shared between multiple threads
         * it needs to be synchronized to avoid inconsistency.
         */
        synchronized (lock) {
            brojacIznajmljivanja++;
            this.redniBrojIznajmljivanja = brojacIznajmljivanja;
        }

        /* Mathematical function for calculating distance between two 2D point in space */
        int udaljenostKretanja = Math.abs(krajnjaLokacija.getKoordinataX() - pocetnaLokacija.getKoordinataX()) +
                Math.abs(krajnjaLokacija.getKoordinataY() - pocetnaLokacija.getKoordinataY());

        /* Based on the distance, it is calculated how much each vehicle is withold on
         * one field of the map
         */
        double trajanjeZadrzavanjaNaPoljuSekunde = (double) trajanjeVoznjeSekunde / udaljenostKretanja;


        /* Print on the console information about start of the current thread */
        System.out.println("=> Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " se nalazi na pocetnoj lokaciji " + pocetnaLokacija);

        /* Get start location, and show vehicle on the GUI based on that location */
        PoljeNaMapi finalPocetnaLokacija = pocetnaLokacija;
        String prikazTeksaNaStartu = "<html>" + prevoznoSredstvo.getJedinstveniIdentifikator() + " - " +
                prevoznoSredstvo.getTrenutniNivoBaterije() + "<br>" + "START";
        SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                prikaziNaMapi(finalPocetnaLokacija, prikazTeksaNaStartu));

        /* Hold the thread(vehicle) on the start position of the map for some time */
        try {
            Thread.sleep((int) (trajanjeZadrzavanjaNaPoljuSekunde * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Remove it from the GUI */
        SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.ukloniSaMape(finalPocetnaLokacija));


        /* Go into alghorithm for calculating the next position of the vehicle(thread) */
        PoljeNaMapi trenutnaLokacija = pocetnaLokacija;

        int i = pocetnaLokacija.getKoordinataX();
        int j = pocetnaLokacija.getKoordinataY();

        /* Crucial variable that enables vehicle to move only in straight lines,
         * by moving alternately between x-axis and y-axis
         */
        boolean naizmjenicno = true;
        boolean iDoKraja = false;
        boolean jdoKraja = false;

        int inkrementPozicijeX = 0;
        int inkrementPozicijeY = 0;


        /* Should vehicle go right or left on the map */
        if(pocetnaLokacija.getKoordinataX() < krajnjaLokacija.getKoordinataX()) {
            inkrementPozicijeX = 1;
        } else if(pocetnaLokacija.getKoordinataX() > krajnjaLokacija.getKoordinataX()) {
            inkrementPozicijeX = -1;
        }

        /* Should vehicle(thread) go up or down on the map */
        if(pocetnaLokacija.getKoordinataY() < krajnjaLokacija.getKoordinataY()) {
            inkrementPozicijeY = 1;
        } else if(pocetnaLokacija.getKoordinataY() > krajnjaLokacija.getKoordinataY()) {
            inkrementPozicijeY = -1;
        }


        while (!trenutnaLokacija.equals(krajnjaLokacija)) {

            /* Do the synchronization to disable multiple access to the Map object */
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

            /* Show current position of the  vehicle on GUI */
            PoljeNaMapi finalTrenutnaLokacija = trenutnaLokacija;
            String tekstZaIspis = "<html>" + prevoznoSredstvo.getJedinstveniIdentifikator() + " - " +  prevoznoSredstvo.getTrenutniNivoBaterije();
            if(finalTrenutnaLokacija.equals(krajnjaLokacija)) {
                tekstZaIspis += "<br>" + "KRAJ";
            }
            final String finalTekstZaIspis = tekstZaIspis;
            SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                    prikaziNaMapi(finalTrenutnaLokacija, finalTekstZaIspis));

            /* Hold the vehicle on this position for calculated time */
            try {
                //Thread.sleep((int)(trajanjeZadrzavanjaNaPoljuSekunde * 100)); // radi brzeg izvrsavanja
                Thread.sleep((int) (trajanjeZadrzavanjaNaPoljuSekunde * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Remove it from the GUI */
            SwingUtilities.invokeLater(() -> Simulacija.grafickiPrikaz.
                    ukloniSaMape(finalTrenutnaLokacija));

            /* Reduce the value of battery for value of 1 on each field it stops */
            try {
                prevoznoSredstvo.umanjiNivoBaterije();
            } catch (PotrosenaBaterijeException e) {
                System.out.println("Prevozno sredstvo " + prevoznoSredstvo + "  nije u mogucnosti da nastavi kretanje");
                return;
            }

            System.out.println("  *** Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " se nalazi na lokaciji " + trenutnaLokacija);
        }

        //System.out.println("=> Vozilo " + prevoznoSredstvo.getJedinstveniIdentifikator() + " je stiglo na odrediste.");

        /* At the end generate the Racun object */
        generisiRacun();

        /* Make sure that multiple thread are not accesing the same File object
         * when writing Racun object into a file
         */
        synchronized (lockPutanjaRacuna) {
            upisiRacun();
        }
    }

}
