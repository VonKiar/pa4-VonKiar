package tracker;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class Reader {

    private String path;

    public Reader(String file) {
        path = file;
    }

    private List<String[]> read(String sourcefile) {
        List<String[]> data = new ArrayList<>();
        String[] readLine = null;
        try (CSVReader reader = new CSVReader(new FileReader(sourcefile))) {
            while((readLine = reader.readNext()) != null) data.add(readLine);
        } catch (Exception e) {System.out.println("Cannot read file: " + sourcefile);}
        return data;
    }

    public String[] getHeader() {
        return getRow(0);
    }

    public String[] getRow(int row){
        return read(this.path).get(row);
    }

    private List<String> getColumn(int column){
        List<String[]> data = read(this.path);
        List<String> index = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) index.add(data.get(i)[column]);
        return index;
    }

    public List<String> getCountry(){
        return getColumn(1);
    }

    public List<String> getState(String country){
        return getColumn(0);
    }
}