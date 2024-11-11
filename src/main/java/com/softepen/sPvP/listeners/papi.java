package com.softepen.sPvP.listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.*;

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
        if (params.equalsIgnoreCase("last_combo")) return criticalHitLastCombo.get(player).toString();
        if (params.equalsIgnoreCase("combo_record")) return criticalHitComboRecord.get(player).toString();
        if (params.equalsIgnoreCase("current_streak")) return killSeries.get(player).toString();
        if (params.equalsIgnoreCase("streak_record")) return killSeriesRecord.get(player).toString();
        if (params.equalsIgnoreCase("rank_ranking")) return String.valueOf(getPlayerRanking(player.getName()));
        if (params.equalsIgnoreCase("kd")) return String.valueOf(getKD(player));

        if (params.startsWith("rank_top_")) {
            String[] paramsList = params.split("_");
            Map.Entry<String, Double> data = getPlayerAtRank(Integer.parseInt(paramsList[2]));
            assert data != null;

            if (Objects.equals(paramsList[3], "points")) return String.valueOf(data.getValue());
            else if (Objects.equals(paramsList[3], "name")) return data.getKey();
        }

        return null;
    }
}
