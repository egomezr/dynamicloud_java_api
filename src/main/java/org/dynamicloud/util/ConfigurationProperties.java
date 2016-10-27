package org.dynamicloud.util;

import org.dynamicloud.logger.LoggerTool;

import java.io.IOException;
import java.util.Properties;

/**
 * This class extends Properties to maintain properties about api configuration.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class ConfigurationProperties extends Properties {
    private static final LoggerTool log = LoggerTool.getLogger(ConfigurationProperties.class);
    public static final String VERSION = "version";

    private static ConfigurationProperties INSTANCE = new ConfigurationProperties();

    public static ConfigurationProperties getInstance() {
        return INSTANCE;
    }

    /**
     * Creates an empty property list with no default values.
     */
    private ConfigurationProperties() {
        try {
            this.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            log.warn("Error loading configuration", e);
        }
    }
}