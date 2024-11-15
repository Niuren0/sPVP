package com.softepen.sPvP.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;

public class OnCommand implements Listener {
    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (frozens.containsKey(player)) {
            String command = event.getMessage().split(" ")[0];

            for (String wlCommand : configManager.getStringList("freeze.whitelistCommands")) {
                if (Objects.equals("/" + wlCommand, command)) return;
            }

            event.setCancelled(true);
            player.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", frozens.get(player).getName()));
        }
    }
}
