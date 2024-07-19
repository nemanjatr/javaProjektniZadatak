import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;
import org.unibl.etf.vozila.PrevoznoSredstvo;

public class Main {
    public static void main(String[] args) {

        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();
        Mapa mapa = new Mapa();

        eMobilityCompany.ucitajPrevoznaSredstvaIzFajla();
        for(PrevoznoSredstvo p : eMobilityCompany.getPrevoznaSredstva()) {
            //System.out.println(p);
        }


        eMobilityCompany.ucitajIznajmljivanjaIzFajla();
        for(Iznajmljivanje i : eMobilityCompany.getIznajmljivanja()) {
            //System.out.println(i);
            i.start();
        }

        for(Iznajmljivanje i : eMobilityCompany.getIznajmljivanja()) {
            //System.out.println(i);
            try{
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }









    }
}