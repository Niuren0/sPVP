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

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.getStreakMessages;
import static com.softepen.sPvP.utils.getSpecialKillMessage;

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

            // KILLER SAVE
            File killerFile = new File(plugin.getDataFolder(), "data/" + killer.getName() + ".yml");
            FileConfiguration killerData = YamlConfiguration.loadConfiguration(killerFile);

            int killerKills = killerData.getInt("kill." + victim.getName(), 0);
            killerData.set("kill." + victim.getName(), killerKills + 1);

            try {
                killerData.save(killerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving killer data file: " + e);
            }

            // VICTIM SAVE
            File victimFile = new File(plugin.getDataFolder(), "data/" + victim.getName() + ".yml");
            FileConfiguration victimData = YamlConfiguration.loadConfiguration(victimFile);

            int victimDeaths = victimData.getInt("death." + victim.getName(), 0);
            victimData.set("death." + victim.getName(), victimDeaths + 1);

            try {
                victimData.save(victimFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving victim data file: " + e);
            }
        }
    }
}
