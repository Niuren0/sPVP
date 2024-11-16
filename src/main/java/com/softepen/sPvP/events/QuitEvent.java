package com.softepen.sPvP.events;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.softepen.sPvP.sPvP.*;

public class QuitEvent implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (frozenPlayers.containsKey(player)) {
            List<String> commands = configManager.getStringList("freeze.commands");
            for (String command : commands) {
                command = command.replace("{playerName}", player.getName());
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
            }
        }

        int lastCombo = criticalHitLastCombo.getOrDefault(player, 0);
        int comboRecord = criticalHitComboRecord.getOrDefault(player, 0);
        int seriesRecord = killSeriesRecord.getOrDefault(player, 0);
        int kill = kills.getOrDefault(player, 0);
        int death = deaths.getOrDefault(player, 0);

        File playerFile = new File(plugin.getDataFolder(), "data/" + player.getName() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

        if (lastCombo > 0) playerData.set("lastCombo", lastCombo);
        if (settings.getComboRecord() < comboRecord) playerData.set("comboRecord", comboRecord);
        if (settings.getKillSeriesRecord() < seriesRecord) playerData.set("killSeriesRecord", seriesRecord);
        playerData.set("kills", kill);
        playerData.set("deaths", death);

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("An error occurred when saving player data file: " + e);
        }
    }
}
