package org.bowssf.util;

import it.uniroma1.lcl.jlt.util.Language;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/**
 * This class gets the configuration data from .ini
 *
 * @author Alejandro Borrajo Viéitez
 */
public class IniConfig {
    private final String PATH = "config/config.ini";
    public final int DEFAULT_LEVELS = 4;
    public final boolean DEFAULT_FORCEBABELNET = false;
    public final boolean DEFAULT_FORCEJEDIS = false;
    public final Language DEFAULT_LANGUAGE = Language.EN;

    private static IniConfig instance;
    private int preloadingLevels;
    private boolean forceBabelNet;
    private boolean forceJedis;

    private Language language;
    private Wini wini;

    /**
     * A singleton from IniConfig
     *
     * @return a objet from the class IniConfig
     */
    public static IniConfig getInstance() {
        if (instance == null) {
            instance = new IniConfig();
        }
        return instance;
    }


    /**
     * Constructor from the clas IniConfig
     */
    private IniConfig() {

        try {
            File file = new File(PATH);
            file.createNewFile();
            wini = new Wini(file);
        } catch (IOException e) {
            this.setPreloadingLevels(DEFAULT_LEVELS);
            this.setForceBabelNet(DEFAULT_FORCEBABELNET);
            this.setForceJedis(DEFAULT_FORCEJEDIS);
            this.setLanguage(DEFAULT_LANGUAGE);
        }


    }

    /**
     * Get the preloading levels for the app
     *
     * @return an integer with the stored preloadling levels
     */
    public int getPreloadingLevels() {
        return preloadingLevels;
    }

    /**
     * Sets the preloaling level value
     *
     * @param preloadingLevels the desired preloading level
     */
    public void setPreloadingLevels(int preloadingLevels) {
        this.preloadingLevels = preloadingLevels;
    }

    /**
     * See if babelnet is checked
     *
     * @return a boolean with a true if the force babel net is check.
     */
    public boolean isForceBabelNet() {
        return forceBabelNet;
    }

    /**
     * Sets the force babel net value
     *
     * @param forceBabelNet the desired value
     */
    public void setForceBabelNet(boolean forceBabelNet) {
        this.forceBabelNet = forceBabelNet;
    }

    /**
     * See if jedis is checked
     *
     * @return a boolean with true if jedis is check.
     */
    public boolean isForceJedis() {
        return forceJedis;
    }

    /**
     * Sets the force jedis value
     *
     * @param forceJedis the desired value
     */
    public void setForceJedis(boolean forceJedis) {
        this.forceJedis = forceJedis;
    }

    /**
     * Gets the language desired
     *
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the language
     *
     * @param language the desired language.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Loads all data from the .ini
     */
    public void load() {
        try {
            this.setPreloadingLevels(wini.get("user", "preloadingLevels", int.class));
            this.setForceJedis(wini.get("user", "forceJedis", boolean.class));
            if (this.isForceJedis()) {
                this.setForceBabelNet(false);
            } else {
                this.setForceBabelNet(wini.get("user", "forceBabelNet", boolean.class));
            }
            String lang = wini.get("user", "language", String.class);
            if (lang.equals("EN")) {
                this.setLanguage(Language.EN);
            } else {
                this.setLanguage(Language.ES);
            }
        } catch (Exception e) {
            this.setPreloadingLevels(DEFAULT_LEVELS);
            this.setForceBabelNet(DEFAULT_FORCEBABELNET);
            this.setForceJedis(DEFAULT_FORCEJEDIS);
            this.setLanguage(DEFAULT_LANGUAGE);
        }

    }

    /**
     * Saves the data in a .ini
     */
    public void save() {

        wini.put("user", "preloadingLevels", this.getPreloadingLevels());
        wini.put("user", "forceJedis", this.isForceJedis());
        wini.put("user", "forceBabelNet", this.isForceBabelNet());
        String lang = Language.EN.toString();
        if (this.getLanguage() == Language.ES) {
            lang = Language.ES.toString();
        }
        wini.put("user", "language", lang);
        try {
            wini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
