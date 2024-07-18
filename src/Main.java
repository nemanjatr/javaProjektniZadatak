import org.unibl.etf.iznajmljivanje.EMobilityCompany;
import org.unibl.etf.iznajmljivanje.Iznajmljivanje;
import org.unibl.etf.vozila.ElektricniAutomobil;
import org.unibl.etf.vozila.ElektricniBicikl;
import org.unibl.etf.vozila.ElektricniTrotinet;
import org.unibl.etf.vozila.PrevoznoSredstvo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        EMobilityCompany eMobilityCompany = EMobilityCompany.getInstanca();

        eMobilityCompany.ucitajPrevoznaSredstvaIzFajla();
        for(PrevoznoSredstvo p : eMobilityCompany.getPrevoznaSredstva()) {
            System.out.println(p);
        }


        eMobilityCompany.ucitajIznajmljivanjaIzFajla();
        for(Iznajmljivanje i : eMobilityCompany.getIznajmljivanja()) {
            System.out.println(i);
        }






    }
}