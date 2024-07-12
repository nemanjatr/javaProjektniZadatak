import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {

        try {
            BufferedReader in = new BufferedReader(new FileReader("prevozna_sredstva.csv"));
            String s;
            while ((s = in.readLine()) != null) {
                String[] karakteristikeVozila = s.split(",");
                for (int i = 0; i < karakteristikeVozila.length-1; i++) {
                    if(karakteristikeVozila[i].isEmpty()){
                        System.out.print("X");
                    }
                    System.out.printf("%-20s", karakteristikeVozila[i]);
                }
                System.out.println();
            }
        in.close();
        } catch (Exception e) {
            e.printStackTrace(); // treba dodati neki specificni exception, a ne samo genericki Exception
        }
    }
}