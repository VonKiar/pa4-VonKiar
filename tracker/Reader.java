package tracker;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class Reader {

    private String path;
    private List<String[]> data;

    public Reader(String file) {
        this.path = file;
        this.data = new ArrayList<>();
        read(this.path);
    }

    private void read(String sourcefile) {
        String[] readLine = null;
        try (CSVReader reader = new CSVReader(new FileReader(sourcefile))) {
            while((readLine = reader.readNext()) != null) this.data.add(readLine);
        } catch (Exception e) {System.out.println("Cannot read file: " + sourcefile);}
    }

    public String[] getHeader() {
        return this.data.get(0);
    }

    public List<String> getCountry(){
        List<String> countryList = new ArrayList<>();
        for(int i = 1; i < this.data.size(); i++) {
            String region = "";
            String compoundR = this.data.get(i)[0];
            if (!compoundR.isBlank()) region = " - " + compoundR;
            countryList.add(data.get(i)[1] + region);
        }
        return countryList;
    }

    public String[] getData(String country, String region) {
        for(int i = 1; i < this.data.size(); i++) {
            if ( !this.data.get(i)[1].equalsIgnoreCase(country)) continue;
            if (region.isBlank() || region.isEmpty()) return this.data.get(i);
            else {
                if (this.data.get(i)[0].equalsIgnoreCase(region)) return this.data.get(i);
            }
        }
        return null;
    }
}