package org.knuth.multimediaremote.server.model.settings;

import org.apache.log4j.Logger;
import org.knuth.multimediaremote.server.Main;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @author Lukas Knuth
 * @version 1.0
 * This class holds the configuration for the Server.
 * <br>
 * It is implemented as a Singleton.
 */
public enum Config {

    /** The singleton-instance. */
    INSTANCE;

    /** The properties-object maintained by this class */
    private Properties props;

    /** Hosts the GUI-representation of the properties */
    private Configurator gui_rep;

    /** Cached base-directory */
    private static File basedir;

    /**
     * Private constructor to enforce singleton-pattern.
     */
    private Config(){
        // TODO Create "Translater"-class with HashMap for all public translations (read only with devensive-copys)
    }

    /**
     * Get a GUI-representation of all properties.
     * @return the gui-representation.
     */
    public JPanel getView(){
        if (gui_rep == null)
            gui_rep = new Configurator();
        return gui_rep.getView();
    }

    /**
     * Sets the property with the given key and it's value.
     * <br>
     * Only for the {@code Configurator}-class in this package!
     * @param key the property-key (name).
     * @param value the properties value.
     */
    void setProperty(String key, String value){
        props.setProperty(key, value);
    }

    /**
     * Searches and returns a value for the given property-
     *  key. <br>
     * If no property was found, it returns an empty String.
     * @param key the properties name.
     * @return the value assigned to the given property-name
     *  or an empty String.
     */
    public String getProperty(String key){
        return props.getProperty(key, "");
    }

    /**
     * Initializes the instance and loads the default- and
     *  custom configurations.
     */
    public void initialize(){
        // Load up the config:
        loadConfig();
    }

    /**
     * This method should <b>always</b> be called when the
     *  application shuts down, as it will do final work
     *  and save the configuration in a file.
     */
    public void close(){
        // Save the current config:
        saveConfig();
    }

    /**
     * Get the path from which this program was started (not
     *  the users home-directory).
     * @return the directory the {@code .jar}-file is in.
     * @see <a href="http://stackoverflow.com/a/6849255/717341">StackOverflow</a>
     */
    public static File getBaseDir(){
        if (basedir != null) return basedir;
        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path.substring(0, path.lastIndexOf("/") + 1);
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            basedir = new File(decodedPath);
            return basedir;
        } catch (UnsupportedEncodingException e) { /* Can't happen */}
        // Otherwise use the Home-directory.
        basedir = new File(".");
        return basedir;
    }

    /**
     * Save the current configuration to the {@code .properties}-file
     *  in the applications root-directory.
     */
    private void saveConfig(){
        File config_file = new File(getBaseDir(), "config.properties");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(config_file);
            props.store(outputStream, "MultiMediaRemote config-file. NOT TO BE CHANGED MANUALLY!");
        } catch (IOException e) {
            Logger logger = Logger.getRootLogger();
            logger.error("Can't write the custom config-file", e);
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Logger logger = Logger.getRootLogger();
                    logger.error("Couldn't close the custom-config files OutputStream", e);
                }
            }
        }
    }

    /**
     * Uses the default configuration and
     *  loads a {@code .properties}-file from the applications
     *  root directory (if there is one).
     */
    private void loadConfig(){
        // Get the default config:
        props = new Properties( defaultConfig() );
        // Check for custom config:
        File config_file = new File(getBaseDir(), "config.properties");
        if (!config_file.exists()) return;
        // If there is one:
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(config_file);
            props.load(inputStream);
            Logger.getLogger("guiLogger").info("User-config successfully loaded");
        } catch (IOException e) {
            // Log the failure:
            Logger logger = Logger.getRootLogger();
            logger.error("Can't load the custom config-file", e);
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Logger logger = Logger.getRootLogger();
                    logger.error("Couldn't close the custom-config files InputStream", e);
                }
            }
        }
    }

    /**
     * This method is <b>not to be called directly!</b> Use the
     *  {@code loadConfig()}-method instead!
     * <br>
     * Loads the default properties from this packages
     *  {@code .properties}-file.
     * <br>
     * This method guaranties to return a working {@code Properties}-
     *  object (even if it's empty).
     * @return a {@code Properties}-object with the applications
     *  default properties.
     */
    private Properties defaultConfig(){
        Properties def = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Config.class.getResourceAsStream("default.properties");
            if (inputStream != null) def.load(inputStream);
            return def;
        } catch (IOException e) {
            // Log the error:
            Logger logger = Logger.getRootLogger();
            logger.error("Can't load default config!", e);
            // Return empty default config:
            return def;
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Log the error.
                    Logger logger = Logger.getRootLogger();
                    logger.error("Couldn't close the default-config files InputStream", e);
                }
            }
        }
    }

}
