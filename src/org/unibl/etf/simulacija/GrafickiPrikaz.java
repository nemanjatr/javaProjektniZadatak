package org.unibl.etf.simulacija;

import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.mapa.Mapa;
import org.unibl.etf.mapa.PoljeNaMapi;
import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;
import org.unibl.etf.iznajmljivanje.Kvar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class for creating GUI of the program. GUI on one side, serves for representing
 * the flow of the simulation that is the movements of the vehicles, and on the other
 * side it used for showcasing some statistical parameters and results of the simulation.
 * <p></p>
 * GrafickiPrikaz class uses Swing API hence why it extends JFrame class.
 *
 *  * @author Nemanja Tripic
 *  * @version 1.0
 *  * @since August 2024
 */
public class GrafickiPrikaz extends JFrame {

    /**
     * JPanel type field, that is the component of the GUI used as a
     * placeholder for menu of options. These options enable the user
     * to choose which statistic or simulation result he wants to see.
     */
    private JPanel panelZaMeni;
    /**
     * JPanel type field, that is the component of the GUI used as a
     * placeholder for map object. On this component the map itself is
     * placed, and vehicle objects are moving on this part of the GUI.
     */
    private JPanel panelZaMapu;

    /**
     * The map is separated into two parts, one part of the map is referenced
     * as narrow part, and these constants define its borders.
     * <ul>
     *   <li>{@code tasterPrevoznaSredstva}: Button for displaying all vehicles in simulation.</li>
     *   <li>{@code tasterKvarovi}: Button for displaying all failures of vehicles in simulation.</li>
     *   <li>{@code tasterRezultatiPoslovanja}: Button for displaying different statistic about business.</li>
     *   <li>{@code tasterSerijalizacija}: Button for displaying results of deserialization, showcasing
     *   some specific statistics.</li>
     *
     * </ul>
     */
    private JButton tasterPrevoznaSredstva;
    private JButton tasterKvarovi;
    private JButton tasterRezultatiPoslovanja;
    private JButton tasterSerijalizacija;


    /**
     * Component on the GUI used that stores fields of the Map object, and
     * prints them on the GUI.
     * Used in-pair with JPanel panelZaMapu object.
     */
    private JLabel[][] prostorZaKretanje;

    /**
     * Contains all vehicles, stored in a HashMap values, with
     * keys being vehicles id.
     */
    private HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva;

    /**
     * List of all vehicles that were successfully rented.
     */
    private ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja;

    /**
     * List of all vehicles that were rented but had a failure.
     */
    private ArrayList<Kvar> iznajmljivanjaSaKvarom;

    /**
     * A HashMap of pair vehicle - its profit. Where for each category
     * there is one vehicle key and its value profit value stored as a double.
     */
    private HashMap<PrevoznoSredstvo, Double> vozilaSaNajvecimPrihodom;


    /**
     * Constructor of the GrafickiPrikaz class.
     *
     * @param svaPrevoznaSredstva HashMap of vehicles, stored in a HashMap values, with keys being vehicles id.
     * @param izvrsenaIznajmljivanja List of all rented vehicles.
     * @param iznajmljivanjaSaKvarom List of all rented vehicles with failure.
     * @param vozilaSaNajvecimPrihodom HashMap of vehicle - profit values from each category.
     */
    public GrafickiPrikaz(HashMap<String, PrevoznoSredstvo> svaPrevoznaSredstva,
                          ArrayList<Iznajmljivanje> izvrsenaIznajmljivanja,
                          ArrayList<Kvar> iznajmljivanjaSaKvarom,
                          HashMap<PrevoznoSredstvo, Double> vozilaSaNajvecimPrihodom) {

        /* Initializatin of fields with parameters */
        this.svaPrevoznaSredstva = svaPrevoznaSredstva;
        this.izvrsenaIznajmljivanja = izvrsenaIznajmljivanja;
        this.iznajmljivanjaSaKvarom = iznajmljivanjaSaKvarom;
        this.vozilaSaNajvecimPrihodom = vozilaSaNajvecimPrihodom;


        /* GUI setup */
        setTitle("E-Mobility Company");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        /* Menu panel setup */
        panelZaMeni = new JPanel();
        panelZaMeni.setBorder(new LineBorder(Color.BLACK, 3));

        /* Setup of button tasterPrevoznaSredstva, using prikazPrevoznihSredstava function */
        tasterPrevoznaSredstva = new JButton("Prevozna Sredstva");
        tasterPrevoznaSredstva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazPrevoznihSredstava();
            }
        });
        /* Adding button to the panel */
        panelZaMeni.add(tasterPrevoznaSredstva);

        /* Setup of button tasterKvarovi, using prikazKvarova function */
        tasterKvarovi = new JButton("Kvarovi");
        tasterKvarovi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazKvarova();
            }
        });

        /* Adding button to the panel */
        panelZaMeni.add(tasterKvarovi);

        /* Setup of button tasterRezultatiPoslovanja, using prikazRezultataPoslovanja function */
        tasterRezultatiPoslovanja = new JButton("Rezultati poslovanja");
        tasterRezultatiPoslovanja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikazRezultataPoslovanja();
            }
        });

        /* Adding button to the panel */
        panelZaMeni.add(tasterRezultatiPoslovanja);


        /* Setup of button tasterSerijalizacija, using prikaziPodatkeDeserijalizacije function */
        tasterSerijalizacija = new JButton("Deserijalizacija");
        tasterSerijalizacija.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prikaziPodatkeDeserijalizacije();
            }
        });

        /* Adding button to the panel */
        panelZaMeni.add(tasterSerijalizacija);




        /* Setup of the panel for the Map */
        panelZaMapu = new JPanel(new GridLayout(20, 20));
        panelZaMapu.setBorder(new LineBorder(Color.BLACK, 3));
        prostorZaKretanje = new JLabel[20][20];

        /* For every field of the map object set appropriate JLabel matrix field
         * narrow part of the map is painted BLUE, and the rest of the Map is in
         * LIGHT_GREY.
         */
        for(int i = 0; i < Mapa.VELICINA_MAPE; i++) {
            for(int j = 0; j < Mapa.VELICINA_MAPE; j++) {
                prostorZaKretanje[i][j] = new JLabel("", SwingConstants.CENTER);
                prostorZaKretanje[i][j].setBorder(new LineBorder(Color.GRAY, 1));
                prostorZaKretanje[i][j].setPreferredSize(new Dimension(20, 20));
                prostorZaKretanje[i][j].setOpaque(true);
                if((i >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && i <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA ) &&
                        (j >= Mapa.UZI_DIO_MAPE_DONJA_GRANICA && j <= Mapa.UZI_DIO_MAPE_GORNJA_GRANICA)) {
                    prostorZaKretanje[i][j].setBackground(Color.BLUE);
                } else {
                    prostorZaKretanje[i][j].setBackground(Color.LIGHT_GRAY);
                }
                /* Add JLabel matrix to the panel */
                panelZaMapu.add(prostorZaKretanje[i][j]);
            }
        }

        /* Add both panels to the contentPane of the frame */
        getContentPane().add(panelZaMeni, BorderLayout.NORTH);
        getContentPane().add(panelZaMapu, BorderLayout.CENTER);
        pack();

    }

    /**
     * Show some text on the map GUI in the selected field.
     * @param poljeZaPrikaz Field of the GUI map on which the text will be displayed.
     * @param tekstZaIspis Text to be displayed.
     */
    public void prikaziNaMapi(PoljeNaMapi poljeZaPrikaz, String tekstZaIspis) {
        int i = poljeZaPrikaz.getKoordinataX();
        int j = poljeZaPrikaz.getKoordinataY();

        /* When desired field is not empty, both previously present text
         * and the new text will be displayed
         */
        if(!(prostorZaKretanje[i][j].getText().isEmpty())) {
            String tekstPreklapanje = prostorZaKretanje[i][j].getText() + "<br>" + tekstZaIspis;
            prostorZaKretanje[i][j].setText(tekstPreklapanje);
        /* When desired field is empty, just display the text and set the field in ORANGE color*/
        } else {
            prostorZaKretanje[i][j].setText(tekstZaIspis);
            prostorZaKretanje[i][j].setBackground(Color.ORANGE);
        }


    }

    /**
     * Function that removes text from the desired field of the map GUI.
     * @param poljeZaUklanjanje Field of the map from which the text needs to
     *                          be removed.
     */
    public void ukloniSaMape(PoljeNaMapi poljeZaUklanjanje) {
        int i = poljeZaUklanjanje.getKoordinataX();
        int j = poljeZaUklanjanje.getKoordinataY();
        prostorZaKretanje[i][j].setText("");
        if(poljeZaUklanjanje.unutarUzegDijelaGrada()) {
            prostorZaKretanje[i][j].setBackground(Color.BLUE);
        } else {
            prostorZaKretanje[i][j].setBackground(Color.LIGHT_GRAY);
        }
    }


    /**
     * Function that is being called when the tasterPrevoznaSredstva button
     * is pressed. It opens a new frame, and creates three tables for each
     * vehicle, and classifies all vehicles into appropriate table.
     */
    private void prikazPrevoznihSredstava() {

        /* Using EventQueue.invokeLater() for synchronisation and
         * responsiveness.
         */
        EventQueue.invokeLater(() -> {

            /* Create new frame and setup it */
            JFrame prozorPrevoznihSredstava = new JFrame("Sva prevozna sredstva");

            JPanel panelZaTabele = new JPanel();
            panelZaTabele.setLayout(new BoxLayout(panelZaTabele, BoxLayout.Y_AXIS));

            /* Automobils */
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
            TitledBorder nazivTabeleAutomobili = BorderFactory.createTitledBorder("Automobili");
            skrolAutomobili.setBorder(nazivTabeleAutomobili);
            panelZaTabele.add(skrolAutomobili);



            /* Bicycles */
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

            /* Scooters */

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


            /* Finishing setup of the frame */
            prozorPrevoznihSredstava.add(panelZaTabele);
            prozorPrevoznihSredstava.setSize(700, 650);
            prozorPrevoznihSredstava.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorPrevoznihSredstava.setVisible(true);

        });
    }


    /**
     * Function that is being called when the tasterKvarovi button
     * is pressed. It opens a new frame, and creates a table of failures,
     * and adds them to it in real-time.
     */
    private void prikazKvarova() {
        EventQueue.invokeLater(() -> {

            JFrame prozorKvarova = new JFrame("Svi kvarovi");

            JPanel panelZaKvarove = new JPanel();
            panelZaKvarove.setLayout(new BoxLayout(panelZaKvarove, BoxLayout.Y_AXIS));

            String[] koloneTabeleKvarova = {"Vrsta Prevoznog Sredstva", "ID Prevoznog Sredstva", "Datum i Vrijeme", "Opis Kvara"};
            if(!iznajmljivanjaSaKvarom.isEmpty()){
                String[][] podaciTabeleKvarova = iznajmljivanjaSaKvarom.stream()
                        .map(kvar -> new String[] {
                                kvar.getVrstaPrevoznogSredstva(),
                                kvar.getIdPrevoznogSredstva(),
                                kvar.getDatumVrijemeKvara().toLocalDate() + " " +
                                        kvar.getDatumVrijemeKvara().toLocalTime(),
                                kvar.getOpisKvara()
                        })
                        .toArray(size -> new String[size][]);

                DefaultTableModel modelTabeleKvarova = new DefaultTableModel(podaciTabeleKvarova, koloneTabeleKvarova);
                JTable tabelaKvarova = new JTable(modelTabeleKvarova);
                tabelaKvarova.setBounds(30, 40, 700, 300);
                JScrollPane skrolKvarova = new JScrollPane(tabelaKvarova);
                TitledBorder nazivTabeleKvarova = BorderFactory.createTitledBorder("Tabela Kvarova");
                skrolKvarova.setBorder(nazivTabeleKvarova);
                panelZaKvarove.add(skrolKvarova);
            }

            prozorKvarova.add(panelZaKvarove);
            prozorKvarova.setSize(700, 300);
            prozorKvarova.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorKvarova.setVisible(true);

        });
    }

    /**
     * Function that is being called when the tasterRezultatiPoslovanja.
     * It creates one table for the summary report with all relevant data.
     * Also for each day it creates a seperate table in which all relevant
     * data for that day are stored.
     */
    private void prikazRezultataPoslovanja() {

        EventQueue.invokeLater(() -> {

            JFrame prozorRezultataPoslovanja = new JFrame("Rezultati poslovanja");

            JPanel panelZaIzvjestaj = new JPanel();
            panelZaIzvjestaj.setLayout(new BoxLayout(panelZaIzvjestaj, BoxLayout.Y_AXIS));



            /* Daily reports */

            /* Group all finished rents by date, and sort them by date and time */
            Map<LocalDate, List<Iznajmljivanje>> grupisanoPoDatumu =
                    izvrsenaIznajmljivanja.stream().collect(Collectors.groupingBy(Iznajmljivanje::getDatum));

            Map<LocalDate, List<Iznajmljivanje>> sortiranaMapa = new TreeMap<>(grupisanoPoDatumu);

            ArrayList<ArrayList<Iznajmljivanje>> listaIznajmljivanjaPoDatumu = new ArrayList<>();
            for(List<Iznajmljivanje> grupa : sortiranaMapa.values()) {
                listaIznajmljivanjaPoDatumu.add(new ArrayList<>(grupa));
            }


            /* Go through all rents and using racunZaPlacanje value from each Iznajmljivanje
            * object calculate data to be displayed.
            */
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
                        if(i.isDesioSeKvar()) {
                            if (i.getPrevoznoSredstvo() instanceof ElektricniAutomobil) {
                                dnevniIznosPopravkeKvarova += 0.07 * i.getPrevoznoSredstvo().getCijenaNabavke();
                            } else if (i.getPrevoznoSredstvo() instanceof ElektricniBicikl) {
                                dnevniIznosPopravkeKvarova += 0.04 * i.getPrevoznoSredstvo().getCijenaNabavke();
                            } else if (i.getPrevoznoSredstvo() instanceof ElektricniTrotinet) {
                                dnevniIznosPopravkeKvarova += 0.02 * i.getPrevoznoSredstvo().getCijenaNabavke();
                            } else {
                                System.out.println("Greska u dnevnim izvjestajima");
                            }
                        }
                        dnevniIznosOdrzavanja = dnevniPrihod * 0.2;
                    }
                }

                String[] koloneDnevnogIzvjestaja = {"Prihod", "Popust", "Promocije", "Iznos", "Odrzavanje", "Kvarovi"};
                String[][] podaciDnevnogIzvjestaja = {
                        {String.format("%.2f", dnevniPrihod), String.format("%.2f", dnevniPopust), String.format("%.2f", dnevnePromocije),
                                String.format("%.2f", dnevniIznosSvihVoznji), String.format("%.2f", dnevniIznosOdrzavanja),
                                String.format("%.2f", dnevniIznosPopravkeKvarova)}
                };

                /* Create the JTable object */
                DefaultTableModel modelTabeleDnevnogIzvjestaja = new DefaultTableModel(podaciDnevnogIzvjestaja, koloneDnevnogIzvjestaja);
                JTable tabelaDnevnogIzvjestaja = new JTable(modelTabeleDnevnogIzvjestaja);
                tabelaDnevnogIzvjestaja.setBounds(30, 40, 200, 100);
                JScrollPane skrolDnevniIzvjestaj = new JScrollPane(tabelaDnevnogIzvjestaja);
                TitledBorder nazivTabele = BorderFactory.createTitledBorder("Dnevni Izvjestaj " + listaPoDatumu.getFirst().getDatum());
                skrolDnevniIzvjestaj.setBorder(nazivTabele);
                panelZaIzvjestaj.add(skrolDnevniIzvjestaj);
            }



            /* Summary report izvjestaj */

            double ukupanPrihod = 0.0;
            double ukupanPopust = 0.0;
            double ukupnoPromocije = 0.0;
            double ukupanIznosSvihVoznji = 0.0;
            double ukupanIznosOdrzavanja = 0.0;
            double ukupanIznosPopravkeKvarova = 0.0;
            double ukupniTroskoviKompanije = 0.0;
            double ukupanProfit = 0.0;
            double ukupanPorez = 0.0;

            /* Go through all finished rents, and calculate the data */
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


            /* Create the JTable object */
            String[] koloneSumarnogIzvjestaja = {"Parametar", "Vrijednost"};
            String[][] podaciSumarnogIzvjestaja = {
                    {"Ukupan prihod", String.format("%.2f", ukupanPrihod)},
                    {"Ukupan popust", String.format("%.2f", ukupanPopust)},
                    {"Ukupno promocije", String.format("%.2f", ukupnoPromocije)},
                    {"Ukupan iznos", String.format("%.2f", ukupanIznosSvihVoznji)},
                    {"Ukupno odrzavanje", String.format("%.2f", ukupanIznosOdrzavanja)},
                    {"Ukupno kvarovi", String.format("%.2f", ukupanIznosPopravkeKvarova)},
                    {"Ukupno troskovi", String.format("%.2f", ukupniTroskoviKompanije)},
                    {"Ukupan porez " , String.format("%.2f", ukupanPorez)}
            };

            DefaultTableModel modelTabeleSumarnogIzvjestaja = new DefaultTableModel(podaciSumarnogIzvjestaja, koloneSumarnogIzvjestaja);
            JTable tabelaSumarnogIzvjestaja = new JTable(modelTabeleSumarnogIzvjestaja);
            tabelaSumarnogIzvjestaja.setBounds(30, 40, 200, 300);
            JScrollPane skrolSumarniIzvjestaj = new JScrollPane(tabelaSumarnogIzvjestaja);
            TitledBorder nazivTabele = BorderFactory.createTitledBorder("Sumarni Izvjestaj");
            skrolSumarniIzvjestaj.setBorder(nazivTabele);
            panelZaIzvjestaj.add(skrolSumarniIzvjestaj);



            /* Add everything to the frame */
            prozorRezultataPoslovanja.add(panelZaIzvjestaj);
            prozorRezultataPoslovanja.setSize(600, 800);
            prozorRezultataPoslovanja.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorRezultataPoslovanja.setVisible(true);

        });
    }


    /**
     * Function that is being called when the tasterSerijalizacija.
     * It displays the data of vehicles with the largest income taken
     * from the vozilaSaNajvecimPrihodom list.
     */
    private void prikaziPodatkeDeserijalizacije() {
        EventQueue.invokeLater(() -> {
            JFrame prozorSerijalizovanihPodataka = new JFrame("Vozila sa najvecim prihodom");

            StringBuilder zaIspis = new StringBuilder("<html>");
            for(Map.Entry<PrevoznoSredstvo, Double> entry :  vozilaSaNajvecimPrihodom.entrySet()) {
                zaIspis.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br>");
            }

            JLabel prostorZaIspis = new JLabel(zaIspis.toString(), SwingConstants.CENTER);

            prozorSerijalizovanihPodataka.add(prostorZaIspis);
            prozorSerijalizovanihPodataka.setSize(500, 100);
            prozorSerijalizovanihPodataka.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            prozorSerijalizovanihPodataka.setVisible(true);

        });
    }

}
