package com.github.davishi25.statcheck.config

import net.minecraftforge.common.config.Configuration

import java.io.File

object Config {
    var apiKey: String? = null
    var debugMode: Boolean = false
    var config: Configuration? = null
    //categories
    const val CATEGORY_GENERAL = "general"

    fun init(configFile: File) {
        if(config == null) {
            config = Configuration(configFile)
            loadConfig()
        }
    }


    fun setKey(newKey: String) {
        apiKey = newKey
        config?.let { cfg ->
            val apiProperty = cfg.get(CATEGORY_GENERAL,"apiKey","")
            apiProperty.set(newKey)
            if (cfg.hasChanged()) cfg.save()
        }
    }

    fun loadConfig() {
        config?.let { cfg ->
            try {
                cfg.load()
                apiKey = cfg.getString(
                    "apiKey",
                    CATEGORY_GENERAL,
                    "",
                    "Your personal API key"
                )
                debugMode = cfg.getBoolean(
                    "debugMode",
                    CATEGORY_GENERAL,
                    false,
                    "Enables debug messages"
                )
            } catch(e: Exception) {
                println("couldn't load mod config")
                e.printStackTrace()
            } finally {
                if (cfg.hasChanged()) cfg.save()
            }
        }
        
    }
}
