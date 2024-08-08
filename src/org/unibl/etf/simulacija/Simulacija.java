package org.unibl.etf.simulacija;

import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;

import javax.swing.*;


public class Simulacija {


    public static GrafickiPrikaz grafickiPrikaz;

    public static void main(String[] args) {


//
//        for(Iznajmljivanje i : eMobilityCompany.izvrsenaIznajmljivanja) {
//            System.out.println(i);
//        }
//
//        //eMobilityCompany.prikazSumarnogIzvjestaja();
//        eMobilityCompany.prikazDnevnihIzvjestaja();

        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();
        Mapa mapa = new Mapa();


        SwingUtilities.invokeLater(() -> {
            grafickiPrikaz = new GrafickiPrikaz(eMobilityCompany.getPrevoznaSredstva(),
                    eMobilityCompany.getIzvrsenaIznajmljivanja(),
                    eMobilityCompany.getIznajmljivanjaSaKvarom(),
                    eMobilityCompany.getVozilaSaNajvecimPrihodom());
            grafickiPrikaz.setVisible(true);
            grafickiPrikaz.setExtendedState(grafickiPrikaz.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        });

        eMobilityCompany.obaviIznajmljivanja();
        eMobilityCompany.pronadjiVozilaSaNajvecimPrihodom();
        eMobilityCompany.deserijalizujVozila();

//        for(Iznajmljivanje i : eMobilityCompany.getIzvrsenaIznajmljivanja()) {
//            System.out.println("Prihod " + i.getPrevoznoSredstvo().getJedinstveniIdentifikator()
//                    + ": " + i.getRacunZaPlacanje().getUkupnoZaPlacanje());
//        }




    }
}