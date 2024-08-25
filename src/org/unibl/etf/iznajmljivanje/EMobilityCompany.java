package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;
import org.unibl.etf.izuzeci.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class EMobilityCompany representing the company itself.
 * It does all the input data handling,renting, scheduling of the rentals,
 * takes care of the process of (de)serialization, all collections needed for the GUI are also
 * filled with methods in this class.
 * The class has a singleton pattern, so it can be instantiated onl once, and that should be done in the
 * main method.
 */
public class EMobilityCompany {

    public static final String AUTOMOBIL = "automobil";
    public static final String BICIKL = "bicikl";
    public static final String TROTINET = "trotinet";
    
    private static final String POGRESNI_PODACI_EXCEPTION = "... Program se nastavlja.";
    private static final String VOZILO_VEC_UCITANO = "Prevozno sredstvo vec ucitano ";
    private static final String ULAZNI_PODACI = "Greska pri ucitavanju ulaznih podataka iz fajla! ";
    private static final String ULAZNI_FAJL = "Greska sa ulaznim fajlom ";
    private static final String NEMOGUCE_IZNAJMLJIVANJE = "Prevozno sredstvo nije moguce iznajmiti, jer ne postoji";


    /* ****************** Singleton pattern ***************************************** */

    /**
     * Private static instance of this class as a part of the singleton pattern.
     */
    private static EMobilityCompany instanca;

    /**
     * Private constructor without parameters as a part of the singleton pattern.
     */
    private EMobilityCompany() {
    }

    /**
     * Getter for the instance of this class, as a part of the singleton pattern.
     *
     * @return Instance of this class.
     */
    public static EMobilityCompany getInstanca() {
        if(instanca == null) {
            instanca = new EMobilityCompany();
        }
        return instanca;
    }
    /* ****************** Singleton pattern ***************************************** */


    /**
     * HashMap field containing all the valid vehicles imported from the file in this class.
     * Key is unique id of the vehicle, and Value is the vehicle itself.
     */
    private HashMap<String, PrevoznoSredstvo> prevoznaSredstva = new HashMap<>();

    /**
     * List of all rentals in the simulation imported from the file in this class.
     */
    private ArrayList<Iznajmljivanje> iznajmljivanja = new ArrayList<>();

    /**
     * List of all the rentals that are successfully executed, i.e. all the rentals with correct
     * input data, and no collision of any kind.
     */
    private ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja = new ArrayList<>();

    /**
     * ArrayList of all the rentals in which failure of the vehicle happened.
     */
    private ArrayList<Kvar> iznajmljivanjaSaKvarom = new ArrayList<>();

    /**
     * HashMap of the vehicle with most profit. Something needed for the serialization part.
     * Key is that vehicle, and Value is Double value containing profit of the vehicle.
     * It should have only three inputs, one for each category of the vehicles.
     */
    HashMap<PrevoznoSredstvo, Double> vozilaSaNajvecimPrihodom = new HashMap<>();


    /**
     * Getter for HashMap field prevoznoSredstvo.
     *
     * @return HashMap field prevoznoSredstvo.
     */
    public HashMap<String, PrevoznoSredstvo> getPrevoznaSredstva() {
        return prevoznaSredstva;
    }

    /**
     * Getter for ArrayList iznajmljivanja.
     *
     * @return ArrayList field iznajmljivanja.
     */
    public ArrayList<Iznajmljivanje> getIznajmljivanja() {
        return iznajmljivanja;
    }

    /**
     * Getter for ArrayList izvrsenaIznajmljivanja.
     *
     * @return ArrayList field izvrsenaIznajmljivanja
     */
    public ArrayList<Iznajmljivanje> getIzvrsenaIznajmljivanja() {
        return izvrsenaIznajmljivanja;
    }

    /**
     * Getter for ArrayList iznajmljivanjaSaKvarom.
     *
     * @return ArrayList field iznajmljivanjaSaKvarom
     */
    public ArrayList<Kvar> getIznajmljivanjaSaKvarom() {
        return iznajmljivanjaSaKvarom;
    }


    /**
     * Getter for HashMap field vozilaSaNajvecimPrihodom.
     *
     * @return HashMap field vozilaSaNajvecimPrihodom.
     */
    public HashMap<PrevoznoSredstvo, Double> getVozilaSaNajvecimPrihodom() {
        return vozilaSaNajvecimPrihodom;
    }


    /**
     * Method that loads .csv file with all vehicles and its data, and parses data into HashMap
     * prevoznaSredstva, by saving a newly create PrevoznoSredstvo as a value, and its id as key.
     * This is done this way, so it can be easily checked whether vehicle with some id is already loaded.
     * It uses regex to check if input data is correct, and based on input data it creates appropriate
     * category of vehicle (car, scooter, bicycle).
     *
     * @throws PogresniUlazniPodaciException Throws this user data exception if input data is not correct.
     */
    public void ucitajPrevoznaSredstvaIzFajla() throws PogresniUlazniPodaciException{

        /* Check if path of the file with input data, provided in inputPath.properties file, is correct */
        File fajlPutanjaPrevoznaSredstva = new File(Iznajmljivanje.inputPathProperties.get("PREVOZNA_SREDSTVA").toString());
        if(!fajlPutanjaPrevoznaSredstva.exists()) {
            throw new PogresniUlazniPodaciException("Fajl sa prevoznim sredstvima ne postoji!");
        }

        /* Read file line by line, and using regex check if all wanted data is present and correct */
        try(BufferedReader citacVozila = new BufferedReader(new FileReader(fajlPutanjaPrevoznaSredstva))) {



            String linijaFajla;
            String regex = "^([A-Za-z0-9]+)," +              // ID
                    "([A-Za-z]+)," +                         // Proizvodjac
                    "([A-Za-z0-9]+)," +                      // Model
                    "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.)?," +  // Datum nabavke (optional)
                    "(\\d+)," +                              // Cijena (mandatory)
                    "(\\d+)?,?" +                            // Domet (optional)
                    "(\\d+)?,?" +                            // Max Brzina (optional)
                    "(.*?)," +                               // Opis (optional)
                    "([A-Za-z]+)$";                          // Vrsta

            Pattern pattern = Pattern.compile(regex);


            linijaFajla = citacVozila.readLine();   /* Read first line to eliminate header */
            while((linijaFajla = citacVozila.readLine()) != null) {
                try {
                    Matcher matcher = pattern.matcher(linijaFajla);

                    if(matcher.matches()) {

                        String jedinstveniIdenitifikator = matcher.group(1);
                        String proizvodjac = matcher.group(2);
                        String model = matcher.group(3);
                        String datumNabavke = matcher.group(4) != null ? matcher.group(4) : "N/A";
                        String cijena = matcher.group(5) != null ? matcher.group(5) : "N/A";
                        String domet = matcher.group(6) != null ? matcher.group(6) : "N/A";
                        String maksimalnaBrzina = matcher.group(7) != null ? matcher.group(7) : "N/A";
                        String opis = matcher.group(8) != null ? matcher.group(8) : "N/A";
                        String vrsta = matcher.group(9);

                        /* Check if vehicle is already loaded */
                        if(!prevoznaSredstva.containsKey(jedinstveniIdenitifikator)) {
                            /* Based on vrsta column from .csv file, classify cars and call appropriate constructor */
                            if(AUTOMOBIL.equals(vrsta)){
                                prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniAutomobil(jedinstveniIdenitifikator, cijena,
                                        proizvodjac, model, datumNabavke, opis));
                            } else if(BICIKL.equals(vrsta)){
                                prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniBicikl(jedinstveniIdenitifikator, cijena,
                                        proizvodjac, model, domet));
                            } else if(TROTINET.equals(vrsta)){
                                prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniTrotinet(jedinstveniIdenitifikator, cijena,
                                        proizvodjac, model, maksimalnaBrzina));
                            }
                        } else {
                            throw new PogresniUlazniPodaciException(VOZILO_VEC_UCITANO + jedinstveniIdenitifikator);
                        }

                    } else {
                        throw new PogresniUlazniPodaciException(ULAZNI_PODACI + linijaFajla);
                    }

                } catch (PogresniUlazniPodaciException e) {
                    System.out.println(POGRESNI_PODACI_EXCEPTION);
                }
            }
        } catch (IOException e) {
            System.out.println(ULAZNI_FAJL);
        }
    }


    /**
     * Method that loads file with all rentals into iznajmljivanja ArrayList. Regex is used the check
     * input data, and csv file is parsed using this regex.
     *
     * @throws PogresniUlazniPodaciException Exception is thrown if input data is incorrect.
     */
    public void ucitajIznajmljivanjaIzFajla() throws PogresniUlazniPodaciException{

        /* Check if path of the file with input data, provided in inputPath.properties file, is correct */
        File fajlPutanjaZaIznajmljivanja = new File(Iznajmljivanje.inputPathProperties.get("IZNAJMLJIVANJA").toString());
        if(!fajlPutanjaZaIznajmljivanja.exists()) {
            throw new PogresniUlazniPodaciException("Ulazni fajl sa iznajmljivanjima ne postoji");
        }

        /* Read file line by line, and parse the line using regex */
        try (BufferedReader citacIznajmljivanja = new BufferedReader(new FileReader(fajlPutanjaZaIznajmljivanja))) {

            String linijaFajla;
            String regex = "^([0-9]{1,2}\\.[0-9]{1,2}\\.\\d{4}\\s[0-9]{2}:[0-9]{2})," +
                    "([A-Za-z0-9]+)," +
                    "([A-Za-z0-9]+)," +
                    "\"([0-9]+,[0-9]+)\"," +
                    "\"([0-9]+,[0-9]+)\"," +
                    "([0-9]+)," +
                    "(ne|da)," +
                    "(ne|da)$";
            Pattern pattern = Pattern.compile(regex);

            linijaFajla = citacIznajmljivanja.readLine();   /* Read first line outside the loop to remove header */
            while ((linijaFajla = citacIznajmljivanja.readLine()) != null) {

                try {
                    Matcher matcher = pattern.matcher(linijaFajla);

                    /* Check the regex, and parse each column into a string */
                    if (matcher.matches()) {

                        String datumVrijeme = matcher.group(1);
                        String imeKorisnika = matcher.group(2);
                        String idPrevoznogSredstva = matcher.group(3);
                        PrevoznoSredstvo prevoznoSredstvo = prevoznaSredstva.get(idPrevoznogSredstva);
                        if (prevoznoSredstvo == null) {
                            throw new PrevoznoSredstvoNePostojiException(NEMOGUCE_IZNAJMLJIVANJE + idPrevoznogSredstva);
                        }
                        String pocetnaLokacija = matcher.group(4);
                        String krajnjaLokacija = matcher.group(5);
                        String trajanjeVoznjeSekunde = matcher.group(6);
                        String kvar = matcher.group(7);
                        String promocija = matcher.group(8);

                        iznajmljivanja.add(new Iznajmljivanje(datumVrijeme, imeKorisnika, prevoznoSredstvo, pocetnaLokacija,
                                krajnjaLokacija, trajanjeVoznjeSekunde, kvar, promocija));

                    } else {
                        throw new PogresniUlazniPodaciException(ULAZNI_PODACI + linijaFajla);
                    }
                } catch (PrevoznoSredstvoNePostojiException | PogresniUlazniPodaciException e) {
                    System.out.println(POGRESNI_PODACI_EXCEPTION);
                }
            }

            /* After iznajmljivanja list is filled, it can be sorted by the date */
            iznajmljivanja.sort(new Comparator<Iznajmljivanje>() {
                @Override
                public int compare(Iznajmljivanje o1, Iznajmljivanje o2) {
                    return o1.getDatumVrijeme().compareTo(o2.getDatumVrijeme());
                }
            });

        } catch (IOException e) {
            System.out.println(ULAZNI_FAJL);
        }
    }

    /**
     * Method that effectively does the renting. Rental should be done in parallel, in a way that all rental
     * that have same date and time, should happen simultaneously. That can be done using threads, and thas is possible
     * becase object Iznajmljivanje is a thread. By grouping all rentals into a HashMap<LocalDateTime, List<Iznajmljivanje>>
     * we get several lists grouped by date and time. Then we can start() just threads from each list, at one time.
     *
     * @throws PogresniUlazniPodaciException Throws user defined exception if input data is not correct.
     */
    public void obaviIznajmljivanja() throws PogresniUlazniPodaciException {

        /* First load all data from input files */
        this.ucitajPrevoznaSredstvaIzFajla();
        this.ucitajIznajmljivanjaIzFajla();

        /* Strings to save driver's license and id document numbers needed to be delivered to the company */
        String vozackaDozvola = "";
        String identifikacioniDokument = "";

        /* Group all rentals based on a date into a HashMap */
        Map<LocalDateTime, List<Iznajmljivanje>> grupisanoPoDatumVrijeme =
                iznajmljivanja.stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatumVrijeme));

        /* Sort all lists in a HashMap */
        Map<LocalDateTime, List<Iznajmljivanje>> sortiranaMapa = new TreeMap<>(grupisanoPoDatumVrijeme);


        /* Put all list from a HashMap sortiranaMapa into a one ArrayList (listaIznajmljivanjaPoDatumVrijeme)
         * where each input is another ArrayList<Iznajmljivanje>
         */
        ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumVrijeme = new ArrayList<>();
        for(List<Iznajmljivanje> grupa : sortiranaMapa.values()) {
            listaIznajmljivanjaPoDatumVrijeme.add(new ArrayList<>(grupa));
        }


        /* Go through one sublist, do some checking to disable multiple rentals with same vehicle,
         * also check if failure happened and then start() al those threads
         */
        for(ArrayList<Iznajmljivanje> podlista : listaIznajmljivanjaPoDatumVrijeme) {
            System.out.println("Grupa " + podlista.getFirst().getDatumVrijeme());

            Map<PrevoznoSredstvo, Integer> brojPonavljanjaUListi = new HashMap<>();

            for(Iznajmljivanje iznajmljivanje : podlista) {
                if(brojPonavljanjaUListi.containsKey(iznajmljivanje.getPrevoznoSredstvo())) {
                    brojPonavljanjaUListi.put(iznajmljivanje.getPrevoznoSredstvo(), brojPonavljanjaUListi.get(iznajmljivanje.getPrevoznoSredstvo()) + 1);
                    System.out.println("Iznajmljivanje: (" + iznajmljivanje + ") nije moguce, jer je vozilo vec iznajmljeno");
                } else {
                    brojPonavljanjaUListi.put(iznajmljivanje.getPrevoznoSredstvo(), 1);
                    izvrsenaIznajmljivanja.add(iznajmljivanje);

                    if(iznajmljivanje.isDesioSeKvar()) {
                        String vrstaPrevoznogSredstvaSaKvarom;
                        if(iznajmljivanje.getPrevoznoSredstvo() instanceof ElektricniAutomobil) {
                            vrstaPrevoznogSredstvaSaKvarom = AUTOMOBIL;
                        } else if(iznajmljivanje.getPrevoznoSredstvo() instanceof ElektricniBicikl) {
                            vrstaPrevoznogSredstvaSaKvarom = BICIKL;
                        } else if(iznajmljivanje.getPrevoznoSredstvo() instanceof ElektricniTrotinet) {
                            vrstaPrevoznogSredstvaSaKvarom = TROTINET;
                        } else {
                            vrstaPrevoznogSredstvaSaKvarom = "Nepoznato prevozno sredstvo";
                        }

                        iznajmljivanjaSaKvarom.add(new Kvar(vrstaPrevoznogSredstvaSaKvarom,
                                    iznajmljivanje.getPrevoznoSredstvo().getJedinstveniIdentifikator(),
                                    iznajmljivanje.getDatumVrijeme(), "opis kvara"));
                    }

                    vozackaDozvola = iznajmljivanje.getKorisnik().getBrojVozackeDozvole();
                    identifikacioniDokument = iznajmljivanje.getKorisnik().getDokument().getBrojDokumenta();

                    iznajmljivanje.start();
                }
            }

            /* Make sure all threads finish before going further */
            try {
                for(Iznajmljivanje i : podlista) {
                    i.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Take a pause between rentals that happened between two different data-time */
            try {
                Thread.sleep(100);   // treba biti 5000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that is searching for vehicle that gain the most profit, where profit is defined
     * as a total sum of Racun object for some Iznajmljivanje object (and every Iznajmljivanje is
     * in correlation with just one vehicle). So the method goes through all finished rentals,
     * and update the value for most gained vehicle.
     * This procedure is done far all three categories of vehicles.
     * HashMap<String, Double> vozilaSaNajvecimPrihodom, has three pairs id - profit, and is updated
     * as new vehicle in some category appears to have bigger profit that the one that is already in the HashMap.
     */
    public void pronadjiVozilaSaNajvecimPrihodom() {

        /* Strings to save id's of vehicles */
        String idAutomobila = "";
        String idBicikla = "";
        String idTrotineta = "";

        double dosadasnjiPrihod = 0.0;

        /* Double values to save profits for each vehicle */
        double najveciPrihodAutomobili = 0.0;
        double najveciPrihodBicikli = 0.0;
        double najveciPrihodTrotineti = 0.0;

        /* HashMap for each category to save vehicles with its profit */
        HashMap<String, Double> prihodiPoAutomobilu = new HashMap<>();
        HashMap<String, Double> prihodiPoBiciklu = new HashMap<>();
        HashMap<String, Double> prihodiPoTrotinetu = new HashMap<>();



        /* Go through all finished rentals, and fill hashmaps for each vehicle type*/
        for(Iznajmljivanje iznajmljivanje : izvrsenaIznajmljivanja) {

            PrevoznoSredstvo ps = iznajmljivanje.getPrevoznoSredstvo();
            double trenutniPrihod = iznajmljivanje.getRacunZaPlacanje().getUkupnoZaPlacanje();

            /* If vehicle is of type ElektricniAutomobil, there are two possible scenarios.
             * Vehicle is already in the hashmap, then check its previous profit, and add to it this new profit.
             * Second scenario is that vehicle is not in the hashMap, then add this vehicle with its current profit
             * into it.
             */
            if(ps instanceof ElektricniAutomobil) {
                if(prihodiPoAutomobilu.containsKey(ps.getJedinstveniIdentifikator())) {
                    dosadasnjiPrihod = prihodiPoAutomobilu.get(ps.getJedinstveniIdentifikator());
                    prihodiPoAutomobilu.put(ps.getJedinstveniIdentifikator(), dosadasnjiPrihod + trenutniPrihod);
                } else {
                    prihodiPoAutomobilu.put(ps.getJedinstveniIdentifikator(), trenutniPrihod);
                }

            /* Same like for ElektricniAutomobil */
            } else if(ps instanceof  ElektricniBicikl) {

               if(prihodiPoBiciklu.containsKey(ps.getJedinstveniIdentifikator())) {
                    dosadasnjiPrihod = prihodiPoBiciklu.get(ps.getJedinstveniIdentifikator());
                    prihodiPoBiciklu.put(ps.getJedinstveniIdentifikator(), dosadasnjiPrihod + trenutniPrihod);
                } else {
                    prihodiPoBiciklu.put(ps.getJedinstveniIdentifikator(), trenutniPrihod);
                }
            /* Again the same */
            } else if(iznajmljivanje.getPrevoznoSredstvo() instanceof ElektricniTrotinet) {

                if(prihodiPoTrotinetu.containsKey(ps.getJedinstveniIdentifikator())) {
                    dosadasnjiPrihod = prihodiPoTrotinetu.get(ps.getJedinstveniIdentifikator());
                    prihodiPoTrotinetu.put(ps.getJedinstveniIdentifikator(), dosadasnjiPrihod + trenutniPrihod);
                } else {
                    prihodiPoTrotinetu.put(ps.getJedinstveniIdentifikator(), trenutniPrihod);
                }

            } else {
                System.out.println("Greska");
            }
        }


        /* Get the biggest value in each of the three hashmaps */

        for(Map.Entry<String, Double> entry :  prihodiPoAutomobilu.entrySet()) {
            if(entry.getValue() > najveciPrihodAutomobili) {
                najveciPrihodAutomobili = entry.getValue();
                idAutomobila = entry.getKey();
            }
        }

        for(Map.Entry<String, Double> entry :  prihodiPoBiciklu.entrySet()) {
            if(entry.getValue() > najveciPrihodBicikli) {
                najveciPrihodBicikli = entry.getValue();
                idBicikla = entry.getKey();
            }
        }


        for(Map.Entry<String, Double> entry :  prihodiPoTrotinetu.entrySet()) {
            if(entry.getValue() > najveciPrihodTrotineti) {
                najveciPrihodTrotineti = entry.getValue();
                idTrotineta = entry.getKey();
            }
        }

        /* Put vehicle of each type into a final HashMap */
        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idAutomobila), najveciPrihodAutomobili);
        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idBicikla), najveciPrihodBicikli);
        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idTrotineta), najveciPrihodTrotineti);


        /* Serialization of these three vehicles from each type */
        for(PrevoznoSredstvo ps : vozilaSaNajvecimPrihodom.keySet()) {
            try(ObjectOutputStream serijalizacija = new ObjectOutputStream(new FileOutputStream
                    (Iznajmljivanje.folderSerijalizacije + File.separator + ps.getJedinstveniIdentifikator() + ".ser"))){

                serijalizacija.writeObject(ps);

            } catch (IOException e) {
                System.out.println();
            }
        }
    }


    /**
     * Method that does deserialization
     */
    public void deserijalizujVozila() {

        File folderSerijalizacije = new File(Iznajmljivanje.outPathProperties.get("SERIJALIZACIJA_PUTANJA").toString());
        File serijalizovaniFajlovi[] = folderSerijalizacije.listFiles();
        if(serijalizovaniFajlovi != null) {
            for(File fajl : serijalizovaniFajlovi) {
                try(ObjectInputStream deserijalizacija = new ObjectInputStream(new FileInputStream(fajl))) {
                    PrevoznoSredstvo ps = (PrevoznoSredstvo) deserijalizacija.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println();
                }
            }
        }
    }
}
