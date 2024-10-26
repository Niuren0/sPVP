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

        if (params.equalsIgnoreCase("kills")) return kills.get(player).toString();
        if (params.equalsIgnoreCase("deaths")) return deaths.get(player).toString();
        if (params.equalsIgnoreCase("current_combo")) return criticalHitCombo.get(player).toString();
        if (params.equalsIgnoreCase("combo_record")) return criticalHitComboRecord.get(player).toString();
        if (params.equalsIgnoreCase("current_streak")) return killSeries.get(player).toString();
        if (params.equalsIgnoreCase("streak_record")) return killSeriesRecord.get(player).toString();

        return null;
    }
}
