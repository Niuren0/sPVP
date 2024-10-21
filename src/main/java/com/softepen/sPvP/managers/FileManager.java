package com.softepen.sPvP.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.plugin;


public class FileManager {

    private final String file;
    private FileConfiguration config;
    private File configFile;

    public FileManager(String file) {
        this.file = file;
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), file);
        if (!configFile.exists()) {
            plugin.saveResource(file, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getPrefixString(String path) {
        String str = Objects.requireNonNull(config.getString(path)).replace("{prefix}", Objects.requireNonNull(config.getString("prefix")));
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String getString(String path) {
        String str = Objects.requireNonNull(config.getString(path));
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public Boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}

