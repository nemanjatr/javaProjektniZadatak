package org.unibl.etf.simulacija;

import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;

import javax.swing.*;


public class Simulacija {


    public static GrafickiPrikaz grafickiPrikaz;

    public static void main(String[] args) {

//        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();
//        Mapa mapa = new Mapa();
//        eMobilityCompany.obaviIznajmljivanja();
//
//        for(Iznajmljivanje i : eMobilityCompany.izvrsenaIznajmljivanja) {
//            System.out.println(i);
//        }
//
//        //eMobilityCompany.prikazSumarnogIzvjestaja();
//        eMobilityCompany.prikazDnevnihIzvjestaja();

        SwingUtilities.invokeLater(() -> {
            grafickiPrikaz = new GrafickiPrikaz();
            grafickiPrikaz.setVisible(true);
            grafickiPrikaz.setExtendedState(grafickiPrikaz.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        });


    }
}