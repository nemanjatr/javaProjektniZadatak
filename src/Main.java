import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        ArrayList<ElektricniAutomobil> automobili = new ArrayList<>();
        ArrayList<ElektricniBicikl> bicikli = new ArrayList<>();
        ArrayList<ElektricniTrotinet> trotineti = new ArrayList<>();
        Set<String> sviJedinstveniId = new HashSet<>();

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
    }
}