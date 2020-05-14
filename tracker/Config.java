package tracker;

import java.io.*;
import java.util.Properties;

/**
 * Config class, managing properties.
 * @author Potchara J.
 * @version FINAL
 */
public class Config {

    /** Attributes */
    private static String PROPERTIESFILES= "tracker/properties/covid.properties";
    private static Config instance = null;
    private Properties props = null;

    /**
     * Constructor, loaf properties file.
     */
    public Config(){
        loadProperties(PROPERTIESFILES);
    }

    /**
     * Get instance of the class [Singleton].
     * @return instance of Config class.
     */
    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    /**
     * Load Properties.
     * @param filename read to read and load properties.
     */
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

    /**
     * Get properties.
     * @return properties.
     */
    public String getProperty(String name) { 
        return props.getProperty(name);
    }
}