package org.unibl.etf.simulacija;

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

    private JLabel[][] prostorZaKretanje;

    public GrafickiPrikaz() {
        setTitle("PJ2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // panel za meni
        panelZaMeni = new JPanel();
        panelZaMeni.setBorder(new LineBorder(Color.BLACK, 3));
        JButton taster = new JButton("test");
        taster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GrafickiPrikaz.this, "Test");
            }
        });
        panelZaMeni.add(taster);



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
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        centeringPanel.add(panelZaMapu);

        // Set a fixed size for the grid panel
        panelZaMapu.setPreferredSize(new Dimension(1000, 1000));



        // dodaj na prozor
        getContentPane().add(panelZaMeni, BorderLayout.NORTH);
        getContentPane().add(centeringPanel, BorderLayout.CENTER);
        pack();

    }

    void kreirajDugme(String naziv, Runnable r){
        JButton button = new JButton(naziv);
        button.addActionListener(e -> r.run());
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        panelZaMeni.add(button);

    }

    synchronized void prikazAutomobila() {
        EventQueue.invokeLater(() -> {
            JFrame jf = new JFrame("Automobili");
            String[] kolone = {"ID","Proizvodjac","Model","Datum nabavke","Cijena nabavke",
                    "Opis"};
            String[][] podaci = new String[6][6];
            DefaultTableModel tableModel = new DefaultTableModel(podaci,kolone) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        });
    }

    synchronized void prikazBicikala() {
        EventQueue.invokeLater(() -> {
            JFrame jf = new JFrame("Bicikl");

        });
    }

}
