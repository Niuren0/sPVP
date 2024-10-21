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

        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

        criticalHitLastCombo.put(player, settings.getLastCombo());
        criticalHitComboRecord.put(player, settings.getComboRecord());
        killSeriesRecord.put(player, settings.getKillSeriesRecord());
        killSeries.put(player, 0);
        kills.put(player, 0);
        deaths.put(player, 0);
    }
}
