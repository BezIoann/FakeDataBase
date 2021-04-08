package DataBase;
import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            int numberOfRecords = Integer.parseInt(args[0]);
            int n = numberOfRecords;
            String[][] data = new String[numberOfRecords][3];
            for(int i = 0; numberOfRecords % 4 > 0; ++i) {
                ++numberOfRecords;
            }
            String region = args[1];
            if (region.equals("en-US") || region.equals("ru-RU") || region.equals("uk-UA")) {

            } else {
                System.out.println("Region should be \"en-US\", \"ru-RU\" or \"uk_UA\"");
                System.exit(0);
            }
            Faker usFaker = new Faker(new Locale(region));
            (new firstThread(data, usFaker, numberOfRecords, region)).start();
            (new secondThread(data, usFaker, numberOfRecords, region)).start();
            (new thridThread(data, usFaker, numberOfRecords, region)).start();
            for(int i = n*3/4; i < n; i++) {
                data[i] = fakeDataUnit(usFaker,region);
            }
            CSVPrinter printer = new CSVPrinter(System.out, CSVFormat.RFC4180.withHeader().withDelimiter(';'));
            for(int i = 0; i < n; i++) {
                printer.printRecord(data[i]);
            }
        } catch (Exception e) {
            System.exit(0);
        }
    }
    public static String[] fakeDataUnit(Faker usFaker, String loc) throws IOException {

        String name = usFaker.name().fullName();
        String addr = usFaker.address().fullAddress();
        String fullAddress =  addr;
        if (loc.equals("ru-RU")) {
            String rs = (Math.random() > 0.5) ?  "Россия " : "РФ ";
            fullAddress = rs + usFaker.address().zipCode() + addr.substring(usFaker.address().fullAddress().indexOf(',',+2));
        } else if (loc.equals("en-US")) {
            fullAddress = (Math.random() > 0.5) ? addr + " US" : addr + " United States";
        } else if (loc.equals("uk-UA")) {
            fullAddress = (Math.random() > 0.5) ? addr + " Україна" : addr + " УКР";
        }

        return  new String[]{name, fullAddress, usFaker.phoneNumber().cellPhone()};
    }
}

class firstThread extends Thread {
    private String[][] data;
    private Faker faker;
    private int n;
    private String region;
    public firstThread(String[][] data, Faker faker,int n, String region){
        this.data = data;
        this.faker = faker;
        this.n = n;
        this.region = region;
    }

    public void run() {
        for(int i = n/4; i < this.n/2; i++) {
            try {
                    this.data[i] = Main.fakeDataUnit(this.faker, this.region);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class secondThread extends Thread {
    private String[][] data;
    private Faker faker;
    private int n;
    private String region;

    public secondThread(String[][] data, Faker faker,int n, String region){
        this.data = data;
        this.faker = faker;
        this.n = n;
        this.region = region;
    }

    public void run() {
        for(int i = 1*n/2; i < 3*this.n/4; i++) {
            try {
                this.data[i] = Main.fakeDataUnit(this.faker, this.region);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class thridThread extends Thread {
    private String[][] data;
    private Faker faker;
    private int n;
    private String region;

    public thridThread(String[][] data, Faker faker,int n, String region){
        this.data = data;
        this.faker = faker;
        this.n = n;
        this.region = region;
    }

    public void run() {
        for(int i = 0; i < n/4; i++) {
            try {
                this.data[i] = Main.fakeDataUnit(this.faker,region);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
