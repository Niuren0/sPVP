package com.softepen.sPvP.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import static com.softepen.sPvP.sPvP.frozens;

public class OnPickup implements Listener {
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        Player player = (Player) event.getEntity();
        if (frozens.containsKey(player)) event.setCancelled(true);
    }
}
