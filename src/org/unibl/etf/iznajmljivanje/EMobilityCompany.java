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

    public static final int BROJ_ULAZNIH_PARAMETARA_PREVOZNA_SREDSTVA = 9;
    public static final int BROJ_ULAZNIH_PARAMETARA_IZNAJMLJIVANJA = 8;

    public static final String FAJL_IZNAJMLJIVANJA = "iznajmljivanja.csv";
    public static final String FAJL_PREVOZNA_SREDSTVA = "prevozna_sredstva.csv";



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
        try{
            File fajlPutanjaZaPrevoznaSredstva = new File(FAJL_PREVOZNA_SREDSTVA);
            BufferedReader citacVozila = new BufferedReader(new FileReader(fajlPutanjaZaPrevoznaSredstva));
            String linijaFajla;

            citacVozila.readLine();
            while((linijaFajla = citacVozila.readLine()) != null) {

                try {

                    String[] karakteristikeVozila = linijaFajla.split(",");

                    if(karakteristikeVozila.length < BROJ_ULAZNIH_PARAMETARA_PREVOZNA_SREDSTVA) {
                        throw new PogresniUlazniPodaciException();
                    }

                    String jedinstveniIdenitifikator = karakteristikeVozila[0];
                    String proizvodjac = karakteristikeVozila[1];
                    String model = karakteristikeVozila[2];
                    String datumNabavke = karakteristikeVozila[3];
                    String cijena = karakteristikeVozila[4];
                    String domet = karakteristikeVozila[5];
                    String maksimalnaBrzina = karakteristikeVozila[6];
                    String opis = karakteristikeVozila[7];
                    String vrsta =  karakteristikeVozila[8];

                    if(jedinstveniIdenitifikator.isEmpty()) {
                        throw new PogresniUlazniPodaciException();
                    }

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
                        System.out.println("Prevozno sredstvo " + jedinstveniIdenitifikator + " vec ucitano.");
                    }
                } catch (PogresniUlazniPodaciException e) {

                }

            }
            citacVozila.close();
        } catch(IOException e) {
            System.out.println("Greska pri ucitavanju prevoznih sredstava iz fajla!");
        }
    }


    public void ucitajIznajmljivanjaIzFajla() {

        File fajlPutanjaZaIznajmljivanja = new File(FAJL_IZNAJMLJIVANJA);
        try (BufferedReader citacIznajmljivanja = new BufferedReader(new FileReader(fajlPutanjaZaIznajmljivanja));) {

            String linijaFajla;
            String regex = "\"([^\"]*)\"|([^,]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;

            ArrayList<String> karakteristikeIznajmljivanja = new ArrayList<>();

            citacIznajmljivanja.readLine();
            while ((linijaFajla = citacIznajmljivanja.readLine()) != null) {

                try {
                    karakteristikeIznajmljivanja.clear();
                    matcher = pattern.matcher(linijaFajla);
                    while (matcher.find()) {
                        if (matcher.group(1) != null) {
                            karakteristikeIznajmljivanja.add(matcher.group(1));
                        } else {
                            karakteristikeIznajmljivanja.add(matcher.group(2));
                        }
                    }

                    try {
                        if (karakteristikeIznajmljivanja.size() < BROJ_ULAZNIH_PARAMETARA_IZNAJMLJIVANJA) {
                            throw new NedovoljnoUlaznihPodatakaException();
                        } else {

                            String datumVrijeme = karakteristikeIznajmljivanja.get(0);
                            String imeKorisnika = karakteristikeIznajmljivanja.get(1);
                            String identifikatorPrevoznogSredstva = karakteristikeIznajmljivanja.get(2);
                            PrevoznoSredstvo prevoznoSredstvo = prevoznaSredstva.get(identifikatorPrevoznogSredstva);
                            if (prevoznoSredstvo == null) {
                                throw new PrevoznoSredstvoNePostojiException("Prevozno sredstvo " +
                                        identifikatorPrevoznogSredstva + " nije moguce iznajmiti, jer ne postoji");
                            }
                            String pocetnaLokacija = karakteristikeIznajmljivanja.get(3);
                            String krajnjaLokacija = karakteristikeIznajmljivanja.get(4);
                            String trajanjeVoznjeSekunde = karakteristikeIznajmljivanja.get(5);
                            String kvar = karakteristikeIznajmljivanja.get(6);
                            String promocija = karakteristikeIznajmljivanja.get(7);

                            try {
                                iznajmljivanja.add(new Iznajmljivanje(datumVrijeme, imeKorisnika, prevoznoSredstvo,
                                        pocetnaLokacija, krajnjaLokacija, trajanjeVoznjeSekunde, kvar, promocija));

                            } catch (PogresniUlazniPodaciException e) {

                            }
                        }
                    } catch (NedovoljnoUlaznihPodatakaException e) {

                    }
                } catch (PrevoznoSredstvoNePostojiException e) {

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
            System.out.println("Greska pri ucitavanju iznajmljivanja iz fajla!");
            e.printStackTrace();
        }
    }

    public void obaviIznajmljivanja() {
        this.ucitajPrevoznaSredstvaIzFajla();
        this.ucitajIznajmljivanjaIzFajla();

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
                Thread.sleep(100);   // treba biti 5000
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
                    (Iznajmljivanje.properties.get("SERIJALIZACIJA_PUTANJA").toString() + ps.getJedinstveniIdentifikator() + ".ser"))){

                serijalizacija.writeObject(ps);

            } catch (IOException e) {
                System.out.println();
            }
        }
    }

    public void deserijalizujVozila() {

        System.out.println("deserijalizacija");
        File folderSerijalizacije = new File(Iznajmljivanje.properties.get("SERIJALIZACIJA_PUTANJA").toString());
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
