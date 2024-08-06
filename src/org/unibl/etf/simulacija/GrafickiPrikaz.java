package org.unibl.etf.simulacija;

import org.unibl.etf.mapa.PoljeNaMapi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GrafickiPrikaz extends JFrame {

    private JPanel panelZaMeni;
    private JPanel panelZaMapu;

    private JButton tasterPrevoznaSredstva;
    private JButton tasterKvarovi;
    private JButton tasterRezultatiPoslovanja;

    private JLabel[][] prostorZaKretanje;

    public GrafickiPrikaz() {
        setTitle("PJ2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // panel za meni
        panelZaMeni = new JPanel();
        panelZaMeni.setBorder(new LineBorder(Color.BLACK, 3));

        tasterPrevoznaSredstva = new JButton("Prevozna Sredstva");
        tasterPrevoznaSredstva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazPrevoznihSredstava();
            }
        });
        panelZaMeni.add(tasterPrevoznaSredstva);

        tasterKvarovi = new JButton("Kvarovi");
        tasterKvarovi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazKvarova();
            }
        });
        panelZaMeni.add(tasterKvarovi);

        tasterRezultatiPoslovanja = new JButton("Rezultati poslovanja");
        tasterRezultatiPoslovanja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazRezultataPoslovanja();
            }
        });
        panelZaMeni.add(tasterRezultatiPoslovanja);






        // panel za mapu
        panelZaMapu = new JPanel(new GridLayout(20, 20));
        panelZaMapu.setBorder(new LineBorder(Color.BLACK, 3));
        prostorZaKretanje = new JLabel[20][20];

        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                prostorZaKretanje[i][j] = new JLabel("", SwingConstants.CENTER);
                prostorZaKretanje[i][j].setBorder(new LineBorder(Color.GRAY, 1));
                prostorZaKretanje[i][j].setPreferredSize(new Dimension(20, 20));
                prostorZaKretanje[i][j].setOpaque(true);
                if((i >= 5 && i <= 14 ) && (j >= 5 && j <= 14)) {
                    prostorZaKretanje[i][j].setBackground(Color.CYAN);
                } else {
                    prostorZaKretanje[i][j].setBackground(Color.LIGHT_GRAY);
                }


                panelZaMapu.add(prostorZaKretanje[i][j]);
            }
        }

        // Wrap the grid panel in another panel to center it
//        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
//        centeringPanel.add(panelZaMapu);
//
//        // Set a fixed size for the grid panel
//        panelZaMapu.setPreferredSize(new Dimension(1000, 1000));



        // dodaj na prozor
        getContentPane().add(panelZaMeni, BorderLayout.NORTH);
        //getContentPane().add(centeringPanel, BorderLayout.CENTER);
        getContentPane().add(panelZaMapu, BorderLayout.CENTER);
        pack();

    }

    public void prikaziNaMapi(PoljeNaMapi poljeZaPrikaz, String tekstZaIspis) {
        int i = poljeZaPrikaz.getKoordinataX();
        int j = poljeZaPrikaz.getKoordinataY();

        if(prostorZaKretanje[i][j].getText() == null) {
            System.out.println("1 " + tekstZaIspis);
        } else if(prostorZaKretanje[i][j].getText().equals("")) {
            System.out.println("2 " + tekstZaIspis);
        } else if(!prostorZaKretanje[i][j].getText().isEmpty()){
            System.out.println("3 " + tekstZaIspis);
        } else {
            System.out.println("4 " + tekstZaIspis);
        }
        //prostorZaKretanje[i][j].setText(tekstZaIspis);

        if(!(prostorZaKretanje[i][j].getText().isEmpty())) {
            String tekstDvaReda = prostorZaKretanje[i][j].getText() + "<br>" + tekstZaIspis;
            prostorZaKretanje[i][j].setText(tekstDvaReda);
            System.out.println(tekstDvaReda);
            System.out.println("************** PORUKA " + tekstZaIspis );
        } else {
            prostorZaKretanje[i][j].setText(tekstZaIspis);
        }


    }

    public void ukloniSaMape(PoljeNaMapi poljeZaUklanjanje) {
        int i = poljeZaUklanjanje.getKoordinataX();
        int j = poljeZaUklanjanje.getKoordinataY();
        prostorZaKretanje[i][j].setText("");
    }

    void prikazPrevoznihSredstava() {
        EventQueue.invokeLater(() -> {
            JFrame prozorPrevoznihSredstava = new JFrame("Sva prevozna sredstva");
            prozorPrevoznihSredstava.setSize(400, 200);
            prozorPrevoznihSredstava.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorPrevoznihSredstava.setVisible(true);

        });
    }

    void prikazKvarova() {
        EventQueue.invokeLater(() -> {
            JFrame prozorKvarova = new JFrame("Svi kvarovi");
            prozorKvarova.setSize(400, 200);
            prozorKvarova.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorKvarova.setVisible(true);

        });
    }

    void prikazRezultataPoslovanja() {
        EventQueue.invokeLater(() -> {
            JFrame prozorPoslovanja = new JFrame("Rezultati poslovanja");
            prozorPoslovanja.setSize(400, 200);
            prozorPoslovanja.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorPoslovanja.setVisible(true);
        });
    }

}
