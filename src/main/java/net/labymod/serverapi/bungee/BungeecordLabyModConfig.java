package net.labymod.serverapi.bungee;

import net.labymod.serverapi.LabyModConfig;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Class created by qlow | Jan
 */
public class BungeecordLabyModConfig extends LabyModConfig {
    private Configuration configuration;

    public BungeecordLabyModConfig(File file) {
        super(file);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        init(file);
    }

    public void init(File file) {
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addDefaults();
        saveConfig();
        loadValues();
    }

    public Object getValue(String key) {
        return this.configuration.get(key);
    }

    public void addDefault(String key, Object value) {
        if (!this.configuration.contains(key))
            this.configuration.set(key, value);
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
