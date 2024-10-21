package com.softepen.sPvP.managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static com.softepen.sPvP.sPvP.configManager;
import static com.softepen.sPvP.sPvP.plugin;

public class PlayerSettingsManager {
    private static final boolean DEFAULT_SOUND = true;
    private static final boolean DEFAULT_HEALTH_INDICATOR = true;
    private static final String DEFAULT_HEALTH_INDICATOR_COLOR = "RED_" + configManager.getString("settingsMenu.color.item");
    private static final boolean DEFAULT_COMBO_MESSAGES = true;
    private static final int DEFAULT_LAST_COMBO = 0;
    private static final int DEFAULT_COMBO_RECORD = 0;
    private static final int DEFAULT_KILL_SERIES_RECORD = 0;
    private static final int DEFAULT_KILLS = 0;
    private static final int DEFAULT_DEATHS = 0;
    private static final String DEFAULT_KILL_MESSAGE = null;

    public static PlayerSettings getPlayerSettings(Player player) {
        String playerName = player.getName();

        File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        boolean sound = playerConfig.getBoolean("sound", DEFAULT_SOUND);
        boolean healthIndicator = playerConfig.getBoolean("healthIndicator", DEFAULT_HEALTH_INDICATOR);
        String healthIndicatorColorName = playerConfig.getString("healthIndicatorColor", DEFAULT_HEALTH_INDICATOR_COLOR);
        Material healthIndicatorColor = Material.valueOf(healthIndicatorColorName);
        boolean comboMessages = playerConfig.getBoolean("comboMessages", DEFAULT_COMBO_MESSAGES);
        int lastCombo = playerConfig.getInt("lastCombo", DEFAULT_LAST_COMBO);
        int comboRecord = playerConfig.getInt("comboRecord", DEFAULT_COMBO_RECORD);
        int killSeriesRecord = playerConfig.getInt("killSeriesRecord", DEFAULT_KILL_SERIES_RECORD);
        int kills = playerConfig.getInt("kills", DEFAULT_KILLS);
        int deaths = playerConfig.getInt("deaths", DEFAULT_DEATHS);
        String killMessage = playerConfig.getString("killMessage", DEFAULT_KILL_MESSAGE);

        playerConfig.set("sound", sound);
        playerConfig.set("healthIndicator", healthIndicator);
        playerConfig.set("healthIndicatorColor", healthIndicatorColor.name());
        playerConfig.set("comboMessages", comboMessages);
        playerConfig.set("lastCombo", lastCombo);
        playerConfig.set("comboRecord", comboRecord);
        playerConfig.set("killSeriesRecord", killSeriesRecord);
        playerConfig.set("kills", kills);
        playerConfig.set("deaths", deaths);
        playerConfig.set("killMessage", killMessage);

        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("An error occurred when saving player data file: " + e);
        }

        return new PlayerSettings(sound, healthIndicator, healthIndicatorColorName, comboMessages, lastCombo, comboRecord, killSeriesRecord, kills, deaths, killMessage);
    }
}
