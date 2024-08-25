package org.unibl.etf.simulacija;

import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.izuzeci.PogresniUlazniPodaciException;
import org.unibl.etf.mapa.Mapa;

import javax.swing.*;


/**
 * Simulation class is the class that contains main method, and from which
 * the program is being executed.
 *
 * @author Nemanja Tripic
 * @version 1.0
 * @since August 2024
 */
public class Simulacija {


    /**
     * A shared custom GUI object for displaying the simulation results.
     * This static variable is initialized in the main method, an is being
     * used to display some relevant reports and simulation regarding
     * E-Mobility company.
     */
    public static GrafickiPrikaz grafickiPrikaz;


    /**
     * Main method in its standard form. Used for initialization of
     * components needed by the simulation, and then starting the simulation
     * by calling methods from EMobilityCompany class.
     * @param args Command line parameters (not used in the simulation)
     */
    public static void main(String[] args) {

        /* Instantiation of EMobilityCompany object, that is the essence of
         * the program and controls the simulation by calling some of his
         * methods
         */
        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();

        /* Mapa object is needed to be initialized before the simulation start
         * and that is being done in its contstructor.
         */
        Mapa mapa = new Mapa();

        /* Calling the relevant methods, they do all the work */
        try {

//            eMobilityCompany.ucitajPrevoznaSredstvaIzFajla();
//            eMobilityCompany.getPrevoznaSredstva().forEach((id, ps) -> System.out.println(ps));

            /* Initialization of static field grafickiPrikaz using Swing API */
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



        } catch (PogresniUlazniPodaciException e) {
            System.out.println("Program se prekida zbog neispravnih ulaznih podataka!");
        }






    }
}