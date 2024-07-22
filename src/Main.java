import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;
import org.unibl.etf.vozila.PrevoznoSredstvo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();
        Mapa mapa = new Mapa();

        eMobilityCompany.ucitajPrevoznaSredstvaIzFajla();
        for(PrevoznoSredstvo p : eMobilityCompany.getPrevoznaSredstva()) {
            //System.out.println(p);
        }


        eMobilityCompany.ucitajIznajmljivanjaIzFajla();
        Map<LocalDateTime, List<Iznajmljivanje>> grupisanoPoDatumVrijeme =
                eMobilityCompany.getIznajmljivanja().stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatumVrijeme));

        ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumVrijeme = new ArrayList<>();
        for(List<Iznajmljivanje> grupa : grupisanoPoDatumVrijeme.values()) {
            listaIznajmljivanjaPoDatumVrijeme.add(new ArrayList<>(grupa));
        }

        for(ArrayList<Iznajmljivanje> podlista : listaIznajmljivanjaPoDatumVrijeme) {
            System.out.println("Grupa " + podlista.getFirst().getDatumVrijeme());

            for(Iznajmljivanje i : podlista) {
                //System.out.println(i);
                i.start();
            }

            try {
                for(Iznajmljivanje i : podlista) {
                    i.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("------------------------------------");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        for(Iznajmljivanje i : eMobilityCompany.getIznajmljivanja()) {
//            //System.out.println(i);
//            i.start();
//        }

//        for(Iznajmljivanje i : eMobilityCompany.getIznajmljivanja()) {
//            //System.out.println(i);
//            try{
//                i.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }









    }
}