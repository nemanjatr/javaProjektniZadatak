package org.unibl.etf.simulacija;

import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GrafickiPrikaz extends JFrame {

    private JPanel panelZaMeni;
    private JPanel panelZaMapu;

    private JButton tasterPrevoznaSredstva;
    private JButton tasterKvarovi;
    private JButton tasterRezultatiPoslovanja;

    private JLabel[][] prostorZaKretanje;

    private HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva;
    private ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja;

    public GrafickiPrikaz(HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva, ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja) {

        this.svaPrevoznaSredstva = svaPrevoznaSredstva;
        this.izvrsenaIznajmljivanja = izvrsenaIznajmljivanja;

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
                prikazSumarnogIzvjestaja();
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

//    void prikazRezultataPoslovanja() {
//        EventQueue.invokeLater(() -> {
//            JFrame prozorPoslovanja = new JFrame("Rezultati poslovanja");
//            prozorPoslovanja.setSize(400, 200);
//            prozorPoslovanja.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            prozorPoslovanja.setVisible(true);
//        });
//    }

    public void prikazDnevnihIzvjestaja() {

        Map<LocalDateTime, List<Iznajmljivanje>> grupisanoPoDatumVrijeme =
                izvrsenaIznajmljivanja.stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatumVrijeme));

        Map<LocalDateTime, List<Iznajmljivanje>> sortiranaMapa = new TreeMap<>(grupisanoPoDatumVrijeme);

        ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumVrijeme = new ArrayList<>();
        for(List<Iznajmljivanje> grupa : sortiranaMapa.values()) {
            listaIznajmljivanjaPoDatumVrijeme.add(new ArrayList<>(grupa));
        }
        // duplirani kod iz metode obaviIznajmljivanja()
        // treba nekako rijesiti

        for(ArrayList<Iznajmljivanje> listaPoDatumu : listaIznajmljivanjaPoDatumVrijeme) {
            double ukupanPrihod = 0.0;
            double ukupanPopust = 0.0;
            double ukupnoPromocije = 0.0;
            double ukupanIznosSvihVoznji = 0.0;
            double ukupanIznosOdrzavanja = 0.0;
            double ukupanIznosPopravkeKvarova = 0.0;

            for(Iznajmljivanje i : listaPoDatumu) {
                ukupanPrihod += i.getRacunZaPlacanje().getUkupnoZaPlacanje();
                ukupanPopust += i.getRacunZaPlacanje().getIznosPopusta();
                ukupnoPromocije += i.getRacunZaPlacanje().getIznosPromocije();
                ukupanIznosSvihVoznji += i.getRacunZaPlacanje().getIznos();
                if(i.getPrevoznoSredstvo() instanceof  ElektricniAutomobil) {
                    ukupanIznosPopravkeKvarova += 0.7 * i.getPrevoznoSredstvo().getCijenaNabavke();
                } else if(i.getPrevoznoSredstvo() instanceof ElektricniBicikl) {
                    ukupanIznosPopravkeKvarova += 0.4 * i.getPrevoznoSredstvo().getCijenaNabavke();
                } else if(i.getPrevoznoSredstvo() instanceof  ElektricniTrotinet) {
                    ukupanIznosPopravkeKvarova += 0.2 * i.getPrevoznoSredstvo().getCijenaNabavke();
                } else {
                    System.out.println("Greska u dnevnim izvjestajima");
                }
                ukupanIznosOdrzavanja = ukupanPrihod * 0.2;
            }

            System.out.println("\nDatum " + listaPoDatumu.get(0).getDatumVrijeme() +
                    "\n\t" + "prihod: " + ukupanPrihod + "\n\tpopust: " + ukupanPopust + "\n\tpromocije: " + ukupnoPromocije +
                    "\n\tiznos svih voznji: " + ukupanIznosSvihVoznji + "\n\todrzavanje: " + ukupanIznosOdrzavanja +
                    "\n\tkvarovi: " + ukupanIznosPopravkeKvarova);
        }
    }

    public void prikazSumarnogIzvjestaja() {

        EventQueue.invokeLater(() -> {

            JFrame prozorRezultataPoslovanja = new JFrame("Rezultati poslovanja");

            JPanel panelZaIzvjestaj = new JPanel();
            panelZaIzvjestaj.setLayout(new BoxLayout(panelZaIzvjestaj, BoxLayout.Y_AXIS));



            /* dnevni izvjestaji */

            Map<LocalDate, List<Iznajmljivanje>> grupisanoPoDatumu =
                    izvrsenaIznajmljivanja.stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatum));

            Map<LocalDate, List<Iznajmljivanje>> sortiranaMapa = new TreeMap<>(grupisanoPoDatumu);

            ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumu = new ArrayList<>();
            for(List<Iznajmljivanje> grupa : sortiranaMapa.values()) {
                listaIznajmljivanjaPoDatumu.add(new ArrayList<>(grupa));
            }
            // duplirani kod iz metode obaviIznajmljivanja()
            // treba nekako rijesiti

            for(ArrayList<Iznajmljivanje> listaPoDatumu : listaIznajmljivanjaPoDatumu) {
                double dnevniPrihod = 0.0;
                double dnevniPopust = 0.0;
                double dnevnePromocije = 0.0;
                double dnevniIznosSvihVoznji = 0.0;
                double dnevniIznosOdrzavanja = 0.0;
                double dnevniIznosPopravkeKvarova = 0.0;

                for (Iznajmljivanje i : listaPoDatumu) {
                    if(i.getRacunZaPlacanje() != null) {
                        dnevniPrihod += i.getRacunZaPlacanje().getUkupnoZaPlacanje();
                        dnevniPopust += i.getRacunZaPlacanje().getIznosPopusta();
                        dnevnePromocije += i.getRacunZaPlacanje().getIznosPromocije();
                        dnevniIznosSvihVoznji += i.getRacunZaPlacanje().getIznos();
                        if (i.getPrevoznoSredstvo() instanceof ElektricniAutomobil) {
                            dnevniIznosPopravkeKvarova += 0.7 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else if (i.getPrevoznoSredstvo() instanceof ElektricniBicikl) {
                            dnevniIznosPopravkeKvarova += 0.4 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else if (i.getPrevoznoSredstvo() instanceof ElektricniTrotinet) {
                            dnevniIznosPopravkeKvarova += 0.2 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else {
                            System.out.println("Greska u dnevnim izvjestajima");
                        }
                        dnevniIznosOdrzavanja = dnevniPrihod * 0.2;
                    }
                }

                String[] koloneDnevnogIzvjestaja = {"Prihod", "Popust", "Promocije", "Iznos", "Odrzavanje", "Kvarovi"};
                String[][] podaciDnevnogIzvjestaja = {
                        {String.valueOf(dnevniPrihod), String.valueOf(dnevniPopust), String.valueOf(dnevnePromocije),
                                String.valueOf(dnevniIznosSvihVoznji), String.valueOf(dnevniIznosOdrzavanja),
                                String.valueOf(dnevniIznosPopravkeKvarova)}
                };

                DefaultTableModel modelTabeleDnevnogIzvjestaja = new DefaultTableModel(podaciDnevnogIzvjestaja, koloneDnevnogIzvjestaja);
                JTable tabelaDnevnogIzvjestaja = new JTable(modelTabeleDnevnogIzvjestaja);
                tabelaDnevnogIzvjestaja.setBounds(30, 40, 200, 100);
                JScrollPane skrolDnevniIzvjestaj = new JScrollPane(tabelaDnevnogIzvjestaja);
                TitledBorder nazivTabele = BorderFactory.createTitledBorder("Dnevni Izvjestaj " + listaPoDatumu.getFirst().getDatum());
                skrolDnevniIzvjestaj.setBorder(nazivTabele);
                panelZaIzvjestaj.add(skrolDnevniIzvjestaj);
            }



            /* ********************** */


            /* Sumarni izvjestaj */

            double ukupanPrihod = 0.0;
            double ukupanPopust = 0.0;
            double ukupnoPromocije = 0.0;
            double ukupanIznosSvihVoznji = 0.0;
            double ukupanIznosOdrzavanja = 0.0;
            double ukupanIznosPopravkeKvarova = 0.0;
            double ukupniTroskoviKompanije = 0.0;
            double ukupanProfit = 0.0;
            double ukupanPorez = 0.0;

            for (Iznajmljivanje i : izvrsenaIznajmljivanja) {
                if(i.getRacunZaPlacanje() != null) {
                    ukupanPrihod += i.getRacunZaPlacanje().getUkupnoZaPlacanje();
                    ukupanPopust += i.getRacunZaPlacanje().getIznosPopusta();
                    ukupnoPromocije += i.getRacunZaPlacanje().getIznosPromocije();
                    ukupanIznosSvihVoznji += i.getRacunZaPlacanje().getIznos();
                    if (i.isDesioSeKvar()) {
                        if (i.getPrevoznoSredstvo() instanceof ElektricniAutomobil) {
                            ukupanIznosPopravkeKvarova += 0.07 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else if (i.getPrevoznoSredstvo() instanceof ElektricniBicikl) {
                            ukupanIznosPopravkeKvarova += 0.04 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else if (i.getPrevoznoSredstvo() instanceof ElektricniTrotinet) {
                            ukupanIznosPopravkeKvarova += 0.02 * i.getPrevoznoSredstvo().getCijenaNabavke();
                        } else {
                            System.out.println("Greska sumarni izvjestaj!");
                        }
                    }
                }
            }

            ukupanIznosOdrzavanja = ukupanPrihod * 0.2;
            ukupniTroskoviKompanije = ukupanIznosOdrzavanja;
            ukupanProfit = ukupanPrihod - ukupanIznosOdrzavanja - ukupanIznosPopravkeKvarova - ukupniTroskoviKompanije;
            if (ukupanProfit > 0.0) {
                ukupanPorez = ukupanProfit * 0.1;
            }


            String[] koloneSumarnogIzvjestaja = {"Parametar", "Vrijednost"};
            String[][] podaciSumarnogIzvjestaja = {
                    {"Ukupan prihod", String.valueOf(ukupanPrihod)},
                    {"Ukupan popust", String.valueOf(ukupanPopust)},
                    {"Ukupno promocije", String.valueOf(ukupnoPromocije)},
                    {"Ukupan iznos", String.valueOf(ukupanIznosSvihVoznji)},
                    {"Ukupno odrzavanje", String.valueOf(ukupanIznosOdrzavanja)},
                    {"Ukupan iznos popravki", String.valueOf(ukupanIznosPopravkeKvarova)},
                    {"Ukupni troskovi", String.valueOf(ukupniTroskoviKompanije)},
                    {"Ukupan porez " , String.valueOf(ukupanPorez)}
            };

            DefaultTableModel modelTabeleSumarnogIzvjestaja = new DefaultTableModel(podaciSumarnogIzvjestaja, koloneSumarnogIzvjestaja);
            JTable tabelaSumarnogIzvjestaja = new JTable(modelTabeleSumarnogIzvjestaja);
            tabelaSumarnogIzvjestaja.setBounds(30, 40, 200, 300);
            JScrollPane skrolSumarniIzvjestaj = new JScrollPane(tabelaSumarnogIzvjestaja);
            TitledBorder nazivTabele = BorderFactory.createTitledBorder("Sumarni Izvjestaj");
            skrolSumarniIzvjestaj.setBorder(nazivTabele);
            panelZaIzvjestaj.add(skrolSumarniIzvjestaj);



            /* dodavanje na glavni prozor */

            prozorRezultataPoslovanja.add(panelZaIzvjestaj);
            prozorRezultataPoslovanja.setSize(600, 500);
            prozorRezultataPoslovanja.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorRezultataPoslovanja.setVisible(true);

        });
    }



}
