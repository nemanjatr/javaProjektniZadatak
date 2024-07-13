import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        ArrayList<ElektricniAutomobil> automobili = new ArrayList<>();
        ArrayList<ElektricniBicikl> bicikli = new ArrayList<>();
        ArrayList<ElektricniTrotinet> trotineti = new ArrayList<>();
        Set<String> sviJedinstveniId = new HashSet<>();


        /* Ucitavanje podataka o prevoznim sredstvima */
        try {
            BufferedReader in = new BufferedReader(new FileReader("prevozna_sredstva.csv"));
            String s;
            in.readLine();
            while ((s = in.readLine()) != null) {

                // parsiranje fajla
                String[] karakteristikeVozila = s.split(",");
                String jedinstveniIdenitifikator = karakteristikeVozila[0];
                String proizvodjac = karakteristikeVozila[1];
                String model = karakteristikeVozila[2];
                String datumNabavke = karakteristikeVozila[3];
                String cijena = karakteristikeVozila[4];
                String domet = karakteristikeVozila[5];
                String maksimalnaBrzina = karakteristikeVozila[6];
                String opis = karakteristikeVozila[7];
                String vrsta =  karakteristikeVozila[8];
                // //

                // prvo provjera da li je input sa istim ID vec ucitan
                if(!sviJedinstveniId.contains(jedinstveniIdenitifikator)){
                    sviJedinstveniId.add(jedinstveniIdenitifikator);

                    if('A' == jedinstveniIdenitifikator.charAt(0)){
                        automobili.add(new ElektricniAutomobil(proizvodjac,
                                Double.parseDouble(cijena), proizvodjac, model,
                                100, datumNabavke, opis));
                    } else if('B' == jedinstveniIdenitifikator.charAt(0)){
                        bicikli.add(new ElektricniBicikl(jedinstveniIdenitifikator, Double.parseDouble(cijena),
                                proizvodjac, model, 100, Integer.parseInt(domet)));
                    } else if('T' == jedinstveniIdenitifikator.charAt(0)){
                        trotineti.add(new ElektricniTrotinet(jedinstveniIdenitifikator, Double.parseDouble(cijena),
                                proizvodjac, model, 100, Integer.parseInt(maksimalnaBrzina)));
                    }
                }
            }
        in.close();
        } catch (Exception e) {
            e.printStackTrace(); // treba dodati neki specificni exception, a ne samo genericki Exception
        }
        /* Ucitavanje podataka o prevoznim sredstvima */


        /******************************************/
        /* Ucitavanje podataka o iznajmljivanjima */
        try{
            BufferedReader in = new BufferedReader(new FileReader("iznajmljivanja.csv"));
            String s = "";

            String datumVrijeme;
            String imeKorisnika;
            String identifikatorPrevoznogSredstva;
            String pocetnaLokacija;
            String krajnjaLokacija;
            String trajanjeVoznjeSekunde;
            String kvar;
            String promocija;


            ArrayList<Iznajmljivanje> listaIznajmljivanja = new ArrayList<>();
            ArrayList<String> karakteristikeIznajmljivanja = new ArrayList<>();

            // procitaj zaglavlje fajla
            in.readLine();

            String regex = "\"([^\"]*)\"|([^,]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;

            while((s = in.readLine()) != null) {
                karakteristikeIznajmljivanja.clear();
                matcher = pattern.matcher(s);
                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        karakteristikeIznajmljivanja.add(matcher.group(1));
                    } else {
                        karakteristikeIznajmljivanja.add(matcher.group(2));
                    }
                }

                datumVrijeme = karakteristikeIznajmljivanja.get(0);
                imeKorisnika = karakteristikeIznajmljivanja.get(1);
                identifikatorPrevoznogSredstva = karakteristikeIznajmljivanja.get(2); // ne salje se u konstruktor Iznajmljivanja
                // jer u tekstu zadatka nije navedeno da se ovaj podataka tu treba cuvati??
                pocetnaLokacija = karakteristikeIznajmljivanja.get(3);
                krajnjaLokacija = karakteristikeIznajmljivanja.get(4);
                trajanjeVoznjeSekunde = karakteristikeIznajmljivanja.get(5);
                kvar = karakteristikeIznajmljivanja.get(6);
                promocija = karakteristikeIznajmljivanja.get(7);


                listaIznajmljivanja.add(new Iznajmljivanje(datumVrijeme, imeKorisnika, identifikatorPrevoznogSredstva,
                        pocetnaLokacija, krajnjaLokacija, trajanjeVoznjeSekunde, kvar, promocija));

            }

            for(int i = 0; i < listaIznajmljivanja.size(); i++) {
                if(true) {
                    listaIznajmljivanja.get(i).generisiRacun();
                }

            }

        } catch(IOException e){
            e.printStackTrace();
        }





    }
}