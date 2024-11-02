package com.softepen.sPvP.events;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.*;

public class DeathEvent implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        killSeries.remove(victim);
        deaths.put(victim, deaths.getOrDefault(victim, 0) + 1);

        Player killer = victim.getKiller();
        if (killer != null) {
            killSeries.put(killer, killSeries.getOrDefault(killer, 0) + 1);
            kills.put(killer, kills.getOrDefault(killer, 0) + 1);

            int currentSeries = killSeries.get(killer);

            if (killSeriesRecord.getOrDefault(killer, 0) < currentSeries) killSeriesRecord.put(killer, currentSeries);

            if (configManager.getBoolean("streakAnnouncements")) {
                String streakMessage = getStreakMessages(currentSeries);
                if (streakMessage != null) {
                    streakMessage = streakMessage
                            .replace("{victim}", victim.getDisplayName())
                            .replace("{killer}", killer.getDisplayName())
                            .replace("{attacker}", killer.getDisplayName());

                    Bukkit.broadcastMessage(streakMessage);
                }
            }

            if (configManager.getBoolean("specialKillMessages")) {
                PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(killer);
                String perm = settings.getKillMessage();
                if (perm != null) {
                    if (killer.hasPermission("spvp.messages." + perm)) {
                        String killMessage = getSpecialKillMessage(perm, killer, victim);
                        if (killMessage != null) Bukkit.broadcastMessage(killMessage);
                        else plugin.getLogger().warning("Special kill messages is null: " + perm);
                    }
                }
            }

            String victimName = victim.getName();
            String killerName = killer.getName();

            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            // KILLER SAVE
            File killerFile = new File(plugin.getDataFolder(), "data/" + killerName + ".yml");
            FileConfiguration killerData = YamlConfiguration.loadConfiguration(killerFile);

            int killerKills = killerData.getInt("kill." + victimName, 0);
            killerData.set("kill." + victimName, killerKills + 1);

            killerData.set("last5kills.5.player", killerData.getString("last5kills.4.player", null));
            killerData.set("last5kills.5.time", killerData.getString("last5kills.4.time", null));

            killerData.set("last5kills.4.player", killerData.getString("last5kills.3.player", null));
            killerData.set("last5kills.4.time", killerData.getString("last5kills.3.time", null));

            killerData.set("last5kills.3.player", killerData.getString("last5kills.2.player", null));
            killerData.set("last5kills.3.time", killerData.getString("last5kills.2.time", null));

            killerData.set("last5kills.2.player", killerData.getString("last5kills.1.player", null));
            killerData.set("last5kills.2.time", killerData.getString("last5kills.1.time", null));

            killerData.set("last5kills.1.player", victimName);
            killerData.set("last5kills.1.time", formattedDateTime);

            try {
                killerData.save(killerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving " + killerName + " killer data file: " + e);
            }

            // VICTIM SAVE
            File victimFile = new File(plugin.getDataFolder(), "data/" + victimName + ".yml");
            FileConfiguration victimData = YamlConfiguration.loadConfiguration(victimFile);

            int victimDeaths = victimData.getInt("death." + killerName, 0);
            victimData.set("death." + killerName, victimDeaths + 1);

            victimData.set("last5deaths.5.player", victimData.getString("last5deaths.4.player", null));
            victimData.set("last5deaths.5.time", victimData.getString("last5deaths.4.time", null));

            victimData.set("last5deaths.4.player", victimData.getString("last5deaths.3.player", null));
            victimData.set("last5deaths.4.time", victimData.getString("last5deaths.3.time", null));

            victimData.set("last5deaths.3.player", victimData.getString("last5deaths.2.player", null));
            victimData.set("last5deaths.3.time", victimData.getString("last5deaths.2.time", null));

            victimData.set("last5deaths.2.player", victimData.getString("last5deaths.1.player", null));
            victimData.set("last5deaths.2.time", victimData.getString("last5deaths.1.time", null));

            victimData.set("last5deaths.1.player", killerName);
            victimData.set("last5deaths.1.time", formattedDateTime);

            try {
                victimData.save(victimFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving " + victimName + " victim data file: " + e);
            }

            // DEATH LOG
            if (configManager.getBoolean("log.enable")) {
                String message = configManager.getString("log.format")
                        .replace("{date}", formattedDateTime)
                        .replace("{killer}", killerName)
                        .replace("{victim}", victimName);

                logMessage(message);
            }
        }
    }
}
