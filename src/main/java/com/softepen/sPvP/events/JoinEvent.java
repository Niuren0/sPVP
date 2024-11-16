package com.softepen.sPvP.events;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.softepen.sPvP.sPvP.*;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerSettings settings;

        if (playerSettings.containsKey(player)) settings = playerSettings.get(player);
        else {
            settings = PlayerSettingsManager.getPlayerSettings(player);
            playerSettings.put(player, settings);
        }

        criticalHitLastCombo.put(player, settings.getLastCombo());
        criticalHitComboRecord.put(player, settings.getComboRecord());
        killSeriesRecord.put(player, settings.getKillSeriesRecord());
        killSeries.put(player, 0);
        kills.put(player, settings.getKills());
        deaths.put(player, settings.getDeaths());

        pointsManager.set("players." + player.getName(), pointsManager.getDouble("players." + player.getName(), 0));
    }
}
