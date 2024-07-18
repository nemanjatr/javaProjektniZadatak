package org.unibl.etf.iznajmljivanje;

import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EMobilityCompany {

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




    public ArrayList<PrevoznoSredstvo> getPrevoznaSredstva() {
        ArrayList<PrevoznoSredstvo> listaPrevoznihSredstava = new ArrayList<>(prevoznaSredstva.values());
        return listaPrevoznihSredstava;
    }

    public ArrayList<Iznajmljivanje> getIznajmljivanja() {
        return iznajmljivanja;
    }

    public void ucitajPrevoznaSredstvaIzFajla() {
        try{
            File fajlPutanjaZaPrevoznaSredstva = new File("prevozna_sredstva.csv");
            BufferedReader citacVozila = new BufferedReader(new FileReader(fajlPutanjaZaPrevoznaSredstva));
            String linijaFajla;



            citacVozila.readLine();
            while((linijaFajla = citacVozila.readLine()) != null) {
                // parsiranje fajla
                String[] karakteristikeVozila = linijaFajla.split(",");

                String jedinstveniIdenitifikator = karakteristikeVozila[0];
                String proizvodjac = karakteristikeVozila[1];
                String model = karakteristikeVozila[2];
                String datumNabavke = karakteristikeVozila[3];
                String cijena = karakteristikeVozila[4];
                String domet = karakteristikeVozila[5];
                String maksimalnaBrzina = karakteristikeVozila[6];
                String opis = karakteristikeVozila[7];
                String vrsta =  karakteristikeVozila[8];

                if(!prevoznaSredstva.containsKey(jedinstveniIdenitifikator)) {
                    if('A' == jedinstveniIdenitifikator.charAt(0)){
                        prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniAutomobil(jedinstveniIdenitifikator, Double.parseDouble(cijena),
                                proizvodjac, model, 100, datumNabavke, opis));
                    } else if('B' == jedinstveniIdenitifikator.charAt(0)){
                        prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniBicikl(jedinstveniIdenitifikator, Double.parseDouble(cijena),
                                proizvodjac, model, 100, Integer.parseInt(domet)));
                    } else if('T' == jedinstveniIdenitifikator.charAt(0)){
                        prevoznaSredstva.put(jedinstveniIdenitifikator, new ElektricniTrotinet(jedinstveniIdenitifikator, Double.parseDouble(cijena),
                                proizvodjac, model, 100, Integer.parseInt(maksimalnaBrzina)));
                    }
                }
            }
            citacVozila.close();
        } catch(IOException e) {
            System.out.println("Greska pri ucitavanju prevoznih sredstava iz fajla!");
            e.printStackTrace();
        }
    }


    public void ucitajIznajmljivanjaIzFajla() {
        try {
            File fajlPutanjaZaIznajmljivanja = new File("iznajmljivanja.csv"); // vjerovatno treba biti static final clan
            BufferedReader citacIznajmljivanja = new BufferedReader(new FileReader(fajlPutanjaZaIznajmljivanja));

            String linijaFajla;
            String regex = "\"([^\"]*)\"|([^,]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;
            ArrayList<String> karakteristikeIznajmljivanja = new ArrayList<>();

            citacIznajmljivanja.readLine();
            while((linijaFajla = citacIznajmljivanja.readLine()) != null) {

                karakteristikeIznajmljivanja.clear();
                matcher = pattern.matcher(linijaFajla);
                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        karakteristikeIznajmljivanja.add(matcher.group(1));
                    } else {
                        karakteristikeIznajmljivanja.add(matcher.group(2));
                    }
                }

                String datumVrijeme = karakteristikeIznajmljivanja.get(0);
                String imeKorisnika = karakteristikeIznajmljivanja.get(1);

                String identifikatorPrevoznogSredstva = karakteristikeIznajmljivanja.get(2); // ne salje se u konstruktor Iznajmljivanja
                // jer u tekstu zadatka nije navedeno da se ovaj podataka tu treba cuvati??
                // !update ->  ipak, na kraju, saljem u konstruktor je mi treba u klasi Iznajmljivanje

                String pocetnaLokacija = karakteristikeIznajmljivanja.get(3);
                String krajnjaLokacija = karakteristikeIznajmljivanja.get(4);
                String trajanjeVoznjeSekunde = karakteristikeIznajmljivanja.get(5);
                String kvar = karakteristikeIznajmljivanja.get(6);
                String promocija = karakteristikeIznajmljivanja.get(7);

                iznajmljivanja.add(new Iznajmljivanje(datumVrijeme, imeKorisnika, identifikatorPrevoznogSredstva,
                        pocetnaLokacija, krajnjaLokacija, trajanjeVoznjeSekunde, kvar, promocija));

            }
            iznajmljivanja.sort(new Comparator<Iznajmljivanje>() {
                @Override
                public int compare(Iznajmljivanje o1, Iznajmljivanje o2) {
                    return o1.getDatumVrijeme().compareTo(o2.getDatumVrijeme());
                }
            });

        } catch(IOException e) {
            System.out.println("Greska pri ucitavanju iznajmljivanja iz fajla!");
            e.printStackTrace();
        }
    }



}
