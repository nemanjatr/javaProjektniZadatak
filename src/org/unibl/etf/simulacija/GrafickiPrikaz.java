package org.unibl.etf.simulacija;

import org.unibl.etf.mapa.PoljeNaMapi;
import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class GrafickiPrikaz extends JFrame {

    private JPanel panelZaMeni;
    private JPanel panelZaMapu;

    private JButton tasterPrevoznaSredstva;
    private JButton tasterKvarovi;
    private JButton tasterRezultatiPoslovanja;

    private JLabel[][] prostorZaKretanje;

    private HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva;

    public GrafickiPrikaz(HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva) {

        this.svaPrevoznaSredstva = svaPrevoznaSredstva;

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

        if(!(prostorZaKretanje[i][j].getText().isEmpty())) {
            String tekstPreklapanje = prostorZaKretanje[i][j].getText() + "<br>" + tekstZaIspis;
            prostorZaKretanje[i][j].setText(tekstPreklapanje);
        } else {
            prostorZaKretanje[i][j].setText(tekstZaIspis);
            prostorZaKretanje[i][j].setBackground(Color.ORANGE);
        }


    }

    public void ukloniSaMape(PoljeNaMapi poljeZaUklanjanje) {
        int i = poljeZaUklanjanje.getKoordinataX();
        int j = poljeZaUklanjanje.getKoordinataY();
        prostorZaKretanje[i][j].setText("");
        if(poljeZaUklanjanje.unutarUzegDijelaGrada()) {
            prostorZaKretanje[i][j].setBackground(Color.CYAN);
        } else {
            prostorZaKretanje[i][j].setBackground(Color.LIGHT_GRAY);
        }
    }


    synchronized void prikazAutomobila() {
        EventQueue.invokeLater(() -> {
            JFrame jf = new JFrame("Automobili");
            List<PrevoznoSredstvo> temp = svaPrevoznaSredstva.values().stream().filter(e ->
                    e instanceof ElektricniAutomobil).toList();
            String[][] podaci = new String[temp.size()][6];
            for(int i = 0;i<temp.size();i++) {
                ElektricniAutomobil trenutniAutomobil = (ElektricniAutomobil) temp.get(i);
                podaci[i] = new String[]{trenutniAutomobil.getJedinstveniIdentifikator(),
                        trenutniAutomobil.getProizvodjac(), trenutniAutomobil.getModel(),
                        trenutniAutomobil.getDatumNabavke().toString(),
                        String.valueOf(trenutniAutomobil.getCijenaNabavke()),
                        trenutniAutomobil.getOpis()};
            }
            String[] kolone = {"ID","Proizvodjac","Model","Datum nabavke","Cijena nabavke",
                    "Opis"};
            DefaultTableModel tableModel = new DefaultTableModel(podaci,kolone) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable jt = new JTable(tableModel);

            jt.setCellSelectionEnabled(true);
            jt.setBounds(30,40,200,300);
            JScrollPane sp=new JScrollPane(jt);
            jf.add(sp);
            jf.setSize(600,400);
            jf.setResizable(false);
            jf.setVisible(true);


        });
    }

    // radiiiiiiiiiiiiiiiiiiiiiiii
//    void prikazPrevoznihSredstava() {
//
//        EventQueue.invokeLater(() -> {
//            JFrame prozorPrevoznihSredstava = new JFrame("Sva prevozna sredstva");
//
//            JPanel panelZaTabele = new JPanel();
//            panelZaTabele.setLayout(new BoxLayout(panelZaTabele, BoxLayout.Y_AXIS));
//
//            /* Automobili */
//            List<PrevoznoSredstvo> temp = svaPrevoznaSredstva.values().stream().filter(e ->
//                    e instanceof ElektricniAutomobil).toList();
//
//            String[][] podaciAutomobila = new String[temp.size()][6];
//            for(int i = 0;i<temp.size();i++) {
//                ElektricniAutomobil trenutniAutomobil = (ElektricniAutomobil) temp.get(i);
//                podaciAutomobila[i] = new String[]{trenutniAutomobil.getJedinstveniIdentifikator(),
//                        trenutniAutomobil.getProizvodjac(), trenutniAutomobil.getModel(),
//                        trenutniAutomobil.getDatumNabavke().toString(),
//                        String.valueOf(trenutniAutomobil.getCijenaNabavke()),
//                        trenutniAutomobil.getOpis()};
//            }
//            String[] kolone = {"ID","Proizvodjac","Model","Datum nabavke","Cijena nabavke",
//                    "Opis"};
//
//
//
//
//            DefaultTableModel modelTabeleAutomobila = new DefaultTableModel(podaciAutomobila, kolone);
//            JTable tabelaAutomobila = new JTable(modelTabeleAutomobila);
//            tabelaAutomobila.setBounds(30, 40, 200, 300);
//            JScrollPane skrolAutomobili = new JScrollPane(tabelaAutomobila);
//            //prozorPrevoznihSredstava.add(skrolAutomobili, BorderLayout.NORTH);
//            TitledBorder nazivTabele = BorderFactory.createTitledBorder("Automobili");
//            skrolAutomobili.setBorder(nazivTabele);
//            panelZaTabele.add(skrolAutomobili);
//
//
//
//            prozorPrevoznihSredstava.add(panelZaTabele);
//            prozorPrevoznihSredstava.setSize(700, 450);
//            prozorPrevoznihSredstava.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            prozorPrevoznihSredstava.setVisible(true);
//
//        });
//    }


    void prikazPrevoznihSredstava() {

        EventQueue.invokeLater(() -> {
            JFrame prozorPrevoznihSredstava = new JFrame("Sva prevozna sredstva");

            JPanel panelZaTabele = new JPanel();
            panelZaTabele.setLayout(new BoxLayout(panelZaTabele, BoxLayout.Y_AXIS));

            /* Automobili */
            List<ElektricniAutomobil> listaAutomobil = svaPrevoznaSredstva.values().stream().filter(e ->
                    e instanceof ElektricniAutomobil).map(e -> (ElektricniAutomobil) e).toList();

            String[][] podaciTabeleAutomobil = listaAutomobil.stream()
                    .map(automobil -> new String[] {
                            automobil.getJedinstveniIdentifikator(),
                            automobil.getProizvodjac(),
                            automobil.getModel(),
                            String.valueOf(automobil.getDatumNabavke()),
                            String.valueOf(automobil.getCijenaNabavke()),
                            automobil.getOpis()
                    })
                    .toArray(size -> new String[size][]);

            String[] koloneTabeleAutomobil = {"ID","Proizvodjac","Model","Datum nabavke","Cijena nabavke",
                    "Opis"};


            DefaultTableModel modelTabeleAutomobila = new DefaultTableModel(podaciTabeleAutomobil, koloneTabeleAutomobil);
            JTable tabelaAutomobila = new JTable(modelTabeleAutomobila);
            tabelaAutomobila.setBounds(30, 40, 200, 300);
            JScrollPane skrolAutomobili = new JScrollPane(tabelaAutomobila);
            //prozorPrevoznihSredstava.add(skrolAutomobili, BorderLayout.NORTH);
            TitledBorder nazivTabele = BorderFactory.createTitledBorder("Automobili");
            skrolAutomobili.setBorder(nazivTabele);
            panelZaTabele.add(skrolAutomobili);



            /* Bicikli */
            List<ElektricniBicikl> listaBicikl = svaPrevoznaSredstva.values().stream().filter(e ->
                    e instanceof  ElektricniBicikl).map(e -> (ElektricniBicikl) e).toList();

            String[] koloneTabeleBicikl = {"ID", "Proizvodjac", "Model", "Cijena", "Domet"};

            String[][] podaciBicikli = listaBicikl.stream()
                    .map(bicikl -> new String[] {
                            bicikl.getJedinstveniIdentifikator(),
                            bicikl.getProizvodjac(),
                            bicikl.getModel(),
                            String.valueOf(bicikl.getCijenaNabavke()),
                            String.valueOf(bicikl.getDomet())
                    })
                    .toArray(size -> new String[size][]);


            DefaultTableModel modelTabeleBicikala = new DefaultTableModel(podaciBicikli, koloneTabeleBicikl);
            JTable tabelaBicikala = new JTable(modelTabeleBicikala);
            tabelaBicikala.setBounds(30, 40, 200, 300);
            JScrollPane skrolBicikala = new JScrollPane(tabelaBicikala);
            //prozorPrevoznihSredstava.add(skrolBicikala, BorderLayout.CENTER);
            TitledBorder nazivTabeleBicikli = BorderFactory.createTitledBorder("Bicikli");
            skrolBicikala.setBorder(nazivTabeleBicikli);
            panelZaTabele.add(skrolBicikala);

            /* Trotineti */

            List<ElektricniTrotinet> listaTrotinet = svaPrevoznaSredstva.values().stream().filter(e ->
                    e instanceof ElektricniTrotinet).map(e -> (ElektricniTrotinet)e).toList();

            String[] koloneTabeleTrotinet = {"ID", "Proizvodjac", "Model", "Cijena", "Maks brzina"};
            String[][] podaciTabeleTrotinet = listaTrotinet.stream()
                    .map(trotinet -> new String[] {
                            trotinet.getJedinstveniIdentifikator(),
                            trotinet.getProizvodjac(),
                            trotinet.getModel(),
                            String.valueOf(trotinet.getCijenaNabavke()),
                            String.valueOf(trotinet.getMaksimalnaBrzina())
                     })
                    .toArray(size -> new String[size][]);

            DefaultTableModel modelTabeleTrotineta = new DefaultTableModel(podaciTabeleTrotinet, koloneTabeleTrotinet);
            JTable tabelaTrotineta = new JTable(modelTabeleTrotineta);
            tabelaTrotineta.setBounds(30, 40, 200, 300);
            JScrollPane skrolTrotineta = new JScrollPane(tabelaTrotineta);
            //prozorPrevoznihSredstava.add(skrolBicikala, BorderLayout.CENTER);
            TitledBorder nazivTabeleTrotineti = BorderFactory.createTitledBorder("Trotineti");
            skrolTrotineta.setBorder(nazivTabeleTrotineti);
            panelZaTabele.add(skrolTrotineta);


            prozorPrevoznihSredstava.add(panelZaTabele);
            prozorPrevoznihSredstava.setSize(700, 650);
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
