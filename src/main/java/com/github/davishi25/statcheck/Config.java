package com.github.davishi25.statcheck;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {
    public static Configuration config;
    //categories
    public static final String CATEGORY_GENERAL = "general";

    public static void init(File configFile) {
        if(config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    public static String apiKey;
    static void setKey(String newKey) {
        apiKey = newKey;
        Property apiProperty = config.get(CATEGORY_GENERAL,"apiKey","");
        apiProperty.set(newKey);
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void loadConfig() {
        try {
            config.load();
            apiKey = config.getString(
                    "apiKey",
                    CATEGORY_GENERAL,
                    "",
                    "Your personal API key"
                    );
        } catch(Exception e) {
            System.err.println("couldn't load mod config");
            e.printStackTrace();
        } finally {
            if(config.hasChanged())
                config.save();
        }
    }
}
