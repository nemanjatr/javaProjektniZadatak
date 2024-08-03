import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;


public class Main {
    public static void main(String[] args) {

        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();
        Mapa mapa = new Mapa();
        eMobilityCompany.obaviIznajmljivanja();




        for(Iznajmljivanje i : eMobilityCompany.izvrsenaIznajmljivanja) {
            System.out.println(i);
        }

        //eMobilityCompany.prikazSumarnogIzvjestaja();
        eMobilityCompany.prikazDnevnihIzvjestaja();





    }
}