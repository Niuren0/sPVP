package com.softepen.sPvP.listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;

public class papi extends PlaceholderExpansion {
    @NotNull
    @Override
    public String getIdentifier() {
        return "spvp";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null) return null;

        if (params.equalsIgnoreCase("lastCritic")) return criticalHitLastCombo.get(player).toString();
        if (params.equalsIgnoreCase("criticalRecord")) return criticalHitComboRecord.get(player).toString();
        if (params.equalsIgnoreCase("killSeries")) return killSeries.get(player).toString();
        if (params.equalsIgnoreCase("killSeriesRecord")) return killSeriesRecord.get(player).toString();
        if (params.equalsIgnoreCase("kills")) return kills.get(player).toString();
        if (params.equalsIgnoreCase("deaths")) return deaths.get(player).toString();

        return null;
    }
}
