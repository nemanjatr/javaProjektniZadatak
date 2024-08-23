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

public class EMobilityCompany {

    public static final String AUTOMOBIL = "automobil";
    public static final String BICIKL = "bicikl";
    public static final String TROTINET = "trotinet";
    
    private static final String POGRESNI_PODACI_EXCEPTION = "... Program se nastavlja.";
    private static final String VOZILO_VEC_UCITANO = "Prevozno sredstvo vec ucitano ";
    private static final String ULAZNI_PODACI = "Greska pri ucitavanju ulaznih podataka iz fajla! ";
    private static final String ULAZNI_FAJL = "Greska sa ulaznim fajlom ";

    private static final String NEMOGUCE_IZNAJMLJIVANJE = "Prevozno sredstvo nije moguce iznajmiti, jer ne postoji";


    /* singleton pattern */
    private static EMobilityCompany instanca;
    private EMobilityCompany() {

    }

    public static EMobilityCompany getInstanca() {
        if(instanca == null) {
            instanca = new EMobilityCompany();
        }
        return instanca;
    }
    /*************************************************/

    private HashMap<String, PrevoznoSredstvo> prevoznaSredstva = new HashMap<>();
    private ArrayList<Iznajmljivanje> iznajmljivanja = new ArrayList<>();
    private ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja = new ArrayList<>();
    private ArrayList<Kvar> iznajmljivanjaSaKvarom = new ArrayList<>();
    HashMap<PrevoznoSredstvo, Double> vozilaSaNajvecimPrihodom = new HashMap<>();



    public HashMap<String, PrevoznoSredstvo> getPrevoznaSredstva() {
        return prevoznaSredstva;
    }

    public ArrayList<Iznajmljivanje> getIznajmljivanja() {
        return iznajmljivanja;
    }

    public ArrayList<Iznajmljivanje> getIzvrsenaIznajmljivanja() {
        return izvrsenaIznajmljivanja;
    }

    public ArrayList<Kvar> getIznajmljivanjaSaKvarom() {
        return iznajmljivanjaSaKvarom;
    }

    public HashMap<PrevoznoSredstvo, Double> getVozilaSaNajvecimPrihodom() {
        return vozilaSaNajvecimPrihodom;
    }

    public void ucitajPrevoznaSredstvaIzFajla() {

        try(BufferedReader citacVozila = new BufferedReader(new FileReader(Iznajmljivanje.inputPathProperties.get("PREVOZNA_SREDSTVA").toString()))) {

            String linijaFajla;
            String regex = "^([A-Za-z0-9]+)," +            // ID
                    "([A-Za-z]+)," +                // Proizvodjac
                    "([A-Za-z0-9]+)," +             // Model
                    "(\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.)?," +  // Datum nabavke (optional)
                    "(\\d+)," +                   // Cijena (mandatory)
                    "(\\d+)?,?" +                   // Domet (optional)
                    "(\\d+)?,?" +                   // Max Brzina (optional)
                    "(.*?)," +                     // Opis (optional)
                    "([A-Za-z]+)$";                // Vrsta

            Pattern pattern = Pattern.compile(regex);

            linijaFajla = citacVozila.readLine();
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

                        if(!prevoznaSredstva.containsKey(jedinstveniIdenitifikator)) {
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


    public void ucitajIznajmljivanjaIzFajla() {

        File fajlPutanjaZaIznajmljivanja = new File(Iznajmljivanje.inputPathProperties.get("IZNAJMLJIVANJA").toString());
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

            linijaFajla = citacIznajmljivanja.readLine();
            while ((linijaFajla = citacIznajmljivanja.readLine()) != null) {

                try {
                    Matcher matcher = pattern.matcher(linijaFajla);

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
            // poslije while petlje sortiranje ArrayList-e
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

    public void obaviIznajmljivanja() {
        this.ucitajPrevoznaSredstvaIzFajla();
        this.ucitajIznajmljivanjaIzFajla();
        String vozackaDozvola = "";
        String identifikacioniDokument = "";

        Map<LocalDateTime, List<Iznajmljivanje>> grupisanoPoDatumVrijeme =
                iznajmljivanja.stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatumVrijeme));

        Map<LocalDateTime, List<Iznajmljivanje>> sortiranaMapa = new TreeMap<>(grupisanoPoDatumVrijeme);

        ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumVrijeme = new ArrayList<>();
        for(List<Iznajmljivanje> grupa : sortiranaMapa.values()) {
            listaIznajmljivanjaPoDatumVrijeme.add(new ArrayList<>(grupa));
        }

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

            try {
                for(Iznajmljivanje i : podlista) {
                    i.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);   // treba biti 5000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pronadjiVozilaSaNajvecimPrihodom() {
        String idAutomobila = "";
        String idBicikla = "";
        String idTrotineta = "";

        double dosadasnjiPrihod = 0.0;

        double najveciPrihodAutomobili = 0.0;
        double najveciPrihodBicikli = 0.0;
        double najveciPrihodTrotineti = 0.0;

        HashMap<String, Double> prihodiPoAutomobilu = new HashMap<>();
        HashMap<String, Double> prihodiPoBiciklu = new HashMap<>();
        HashMap<String, Double> prihodiPoTrotinetu = new HashMap<>();



        for(Iznajmljivanje iznajmljivanje : izvrsenaIznajmljivanja) {
            PrevoznoSredstvo ps = iznajmljivanje.getPrevoznoSredstvo();
            double trenutniPrihod = iznajmljivanje.getRacunZaPlacanje().getUkupnoZaPlacanje();

            if(ps instanceof ElektricniAutomobil) {
                if(prihodiPoAutomobilu.containsKey(ps.getJedinstveniIdentifikator())) {
                    dosadasnjiPrihod = prihodiPoAutomobilu.get(ps.getJedinstveniIdentifikator());
                    prihodiPoAutomobilu.put(ps.getJedinstveniIdentifikator(), dosadasnjiPrihod + trenutniPrihod);
                } else {
                    prihodiPoAutomobilu.put(ps.getJedinstveniIdentifikator(), trenutniPrihod);
                }


            } else if(ps instanceof  ElektricniBicikl) {

               if(prihodiPoBiciklu.containsKey(ps.getJedinstveniIdentifikator())) {
                    dosadasnjiPrihod = prihodiPoBiciklu.get(ps.getJedinstveniIdentifikator());
                    prihodiPoBiciklu.put(ps.getJedinstveniIdentifikator(), dosadasnjiPrihod + trenutniPrihod);
                } else {
                    prihodiPoBiciklu.put(ps.getJedinstveniIdentifikator(), trenutniPrihod);
                }

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

        System.out.println("serijalizacija : " + idAutomobila + " " + najveciPrihodAutomobili + " " +
                idBicikla + " " + najveciPrihodBicikli + " " +
                idTrotineta + " " + najveciPrihodTrotineti);


        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idAutomobila), najveciPrihodAutomobili);
        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idBicikla), najveciPrihodBicikli);
        vozilaSaNajvecimPrihodom.put(prevoznaSredstva.get(idTrotineta), najveciPrihodTrotineti);

        for(PrevoznoSredstvo ps : vozilaSaNajvecimPrihodom.keySet()) {
            try(ObjectOutputStream serijalizacija = new ObjectOutputStream(new FileOutputStream
                    (Iznajmljivanje.outPathProperties.get("SERIJALIZACIJA_PUTANJA").toString() + ps.getJedinstveniIdentifikator() + ".ser"))){

                serijalizacija.writeObject(ps);

            } catch (IOException e) {
                System.out.println();
            }
        }
    }

    public void deserijalizujVozila() {

        System.out.println("deserijalizacija");
        File folderSerijalizacije = new File(Iznajmljivanje.outPathProperties.get("SERIJALIZACIJA_PUTANJA").toString());
        File serijalizovaniFajlovi[] = folderSerijalizacije.listFiles();
        if(serijalizovaniFajlovi != null) {
            for(File fajl : serijalizovaniFajlovi) {
                try(ObjectInputStream deserijalizacija = new ObjectInputStream(new FileInputStream(fajl))) {
                    PrevoznoSredstvo ps = (PrevoznoSredstvo) deserijalizacija.readObject();
                    System.out.println(ps.getJedinstveniIdentifikator() + vozilaSaNajvecimPrihodom.get(ps));
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println();
                }
            }
        }
    }
}
