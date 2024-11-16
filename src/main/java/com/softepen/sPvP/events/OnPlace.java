package com.softepen.sPvP.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.softepen.sPvP.sPvP.frozenPlayers;
import static com.softepen.sPvP.sPvP.messagesManager;

public class OnPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.containsKey(player)) {
            event.setCancelled(true);
            player.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", frozenPlayers.get(player).getName()));
        }
    }
}
