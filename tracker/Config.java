package tracker;

import java.io.*;
import java.util.Properties;

public class Config {

    private static String PROPERTIESFILES= "tracker/properties/covid.properties";
    private static Config instance = null;
    private Properties props = null;

    public Config(){
        loadProperties(PROPERTIESFILES);
    }

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    private void loadProperties(String filename){
        props = new Properties();
        InputStream instream = null;
        ClassLoader loader = this.getClass().getClassLoader();
        instream = loader.getResourceAsStream(filename);
        
        if(instream == null){
            System.err.println("Unable to open properties file "+filename);
            return;
        }
        try{
            props.load(instream);
        }
        catch(IOException e){
            System.err.println("Error reading properties file "+filename);
            System.err.println(e.getMessage());
        }
        try {
            instream.close();
        } catch (IOException ioe) {

        }
    }

    public String getProperty(String name) { 
        return props.getProperty(name);
    }

}