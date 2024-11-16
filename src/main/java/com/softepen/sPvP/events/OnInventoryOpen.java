package com.softepen.sPvP.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import static com.softepen.sPvP.sPvP.frozenPlayers;
import static com.softepen.sPvP.sPvP.messagesManager;

public class OnInventoryOpen implements Listener {
    @EventHandler
    public void onPlayerInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if (frozenPlayers.containsKey(player)) {
            event.setCancelled(true);
            player.closeInventory();
            player.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", frozenPlayers.get(player).getName()));
        }
    }
}
