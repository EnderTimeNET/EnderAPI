package net.labymod.serverapi.bukkit;

import net.labymod.serverapi.LabyModConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BukkitLabyModConfig extends LabyModConfig {
    private FileConfiguration fileConfiguration;

    public BukkitLabyModConfig(File file) {
        super(file);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        this.fileConfiguration = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
        init(file);
    }

    public void init(File file) {
        this.fileConfiguration.options().copyDefaults(true);
        addDefaults();
        saveConfig();
        loadValues();
    }

    public Object getValue(String key) {
        return this.fileConfiguration.get(key);
    }

    public void addDefault(String key, Object value) {
        this.fileConfiguration.addDefault(key, value);
    }

    public void saveConfig() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
