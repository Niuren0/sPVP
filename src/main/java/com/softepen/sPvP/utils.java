package com.softepen.sPvP;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import com.softepen.sPvP.managers.RankManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.softepen.sPvP.sPvP.*;

public class utils {
    public static Sound getBreakSound(Sound defaultSound) {
        String soundName = configManager.getString("sounds.critical_break");
        try {
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultSound;
        }
    }

    public static Sound getComboSound(int combo, Player player) {
        try {
            String path = PlayerSettingsManager.getPlayerSettings(player).getComboSound();
            String soundName = soundsManager.getString(path + "." + combo + ".sound");
            if (soundName == null) soundName = soundsManager.getString(path + ".default.sound");
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException | NullPointerException e) {
            plugin.getLogger().warning("Error at " + player.getName() + "'s getComboSound function, using default sound :" + e);
            return Sound.valueOf(soundsManager.getString(configManager.getString("sounds.critical_hit_default") + ".default.sound"));
        }
    }

    public static float getComboSoundPitch(int combo, Player player) {
        try {
            String path = PlayerSettingsManager.getPlayerSettings(player).getComboSound();
            return (float) soundsManager.getDouble(path + "." + combo + ".pitch", 1);
        } catch (IllegalArgumentException | NullPointerException e) {
            return 1;
        }
    }

    public static String getComboMessage(int combo) {
        ConfigurationSection combos = messagesManager.getConfigurationSection("comboMessages");
        if (!combos.contains(String.valueOf(combo))) return null;

        return messagesManager.getPrefixString("comboMessages." + combo);
    }

    public static String getStreakMessages(int series) {
        ConfigurationSection combos = messagesManager.getConfigurationSection("streakMessages");
        if (!combos.contains(String.valueOf(series))) return null;

        return messagesManager.getPrefixString("streakMessages." + series);
    }

    public static String getSpecialKillMessage(String perm, Player killer, Player victim) {
        String killMessage = messagesManager.getString("specialKillMessages." + perm);
        String message = messagesManager.getPrefixString("specialKillMessageFormat");
        if (message == null) return null;

        message = message.replace("{message}", killMessage);

        message = message
                .replace("{killer}", killer.getDisplayName())
                .replace("{attacker}", killer.getDisplayName())
                .replace("{victim}", victim.getDisplayName());

        return message;
    }

    public static List<String> applyTop5(String type, List<String> messages, String playerName) {
        File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        String path = Objects.equals(type, "top5deaths") ? "death" : "kill";

        Map<String, Integer> counts = new HashMap<>();
        if (playerData.contains(path)) {
            ConfigurationSection section = playerData.getConfigurationSection(path);
            if (section != null) {
                for (String player : section.getKeys(false)) {
                    counts.put(player, playerData.getInt(path + "." + player));
                }
            }
        }

        List<Map.Entry<String, Integer>> topEntries = counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();

        return getStrings(messages, topEntries);
    }

    @NotNull
    private static List<String> getStrings(List<String> messages, List<Map.Entry<String, Integer>> topEntries) {
        List<String> updatedMessages = new ArrayList<>();

        for (String message : messages) {
            String updatedMessage = message;

            for (int i = 0; i < topEntries.size(); i++) {
                Map.Entry<String, Integer> entry = topEntries.get(i);
                String playerRank = entry.getKey();
                int countRank = entry.getValue();

                updatedMessage = updatedMessage.replace("{" + (i + 1) + "}", playerRank)
                        .replace("{count" + (i + 1) + "}", String.valueOf(countRank));
            }

            for (int i = topEntries.size(); i < 5; i++) {
                updatedMessage = updatedMessage.replace("{" + (i + 1) + "}", "-")
                        .replace("{count" + (i + 1) + "}", "0");
            }

            updatedMessages.add(updatedMessage);
        }
        return updatedMessages;
    }

    public static List<String> applyLast5(String path, List<String> messages, String playerName) {
        File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        List<String> updatedMessages = new ArrayList<>();

        for (String message: messages) {
            String updatedMessage = message;

            for (int i = 1; i <= 5; i++) {
                updatedMessage = updatedMessage.replace("{" + i + "}", playerData.getString(path + "." + i + ".player", "-"));
                updatedMessage = updatedMessage.replace("{time" + i + "}", playerData.getString(path + "." + i + ".time", "-"));
            }

            updatedMessages.add(updatedMessage);
        }

        return updatedMessages;
    }

    public static void logMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            message = message.replace("{timestamp}", timestamp);

            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            plugin.getLogger().severe("Log file could not write: " + e.getMessage());
        }
    }

    public static boolean isPlayerInDisabledRegion(Player player, String path) {
        if (!wgExpansion) return false;

        List<String> disabledRegions = configManager.getStringList("disabledRegions." + path);
        RegionContainer container = WGPlugin.getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regions != null) {
            ApplicableRegionSet regionSet = regions.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
            for (ProtectedRegion region : regionSet) {
                if (disabledRegions.contains(region.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPlayerInDisabledWorld(Player player, String path) {
        List<String> disabledWorlds = configManager.getStringList("disabledWorld." + path);

        String playerWorldName = player.getWorld().getName();

        return disabledWorlds.contains(playerWorldName);
    }

    public static String getPlayerRank(String playerName) {
        double playerExp = new RankManager(playerName).getPoints();
        String defaultRank = configManager.getString("rank.defaultRank");

        for (Map.Entry<String, Double> entry : ranks.entrySet()) {
            String rank = entry.getKey();
            double rankExp = entry.getValue();
            if (rankExp > playerExp) return defaultRank;
            else defaultRank = rank;
        }

        return defaultRank;
    }

    public static void ranksReload() {
        if (ranks != null) ranks.clear();
        List<String> rankList = getAllSection("ranks");
        for (String rank : rankList) {
            double rankExp = ranksManager.getDouble("ranks." + rank + ".exp");
            ranks.put(rank, rankExp);
        }
        ranks = ranks.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static List<String> getAllSection(String path) {
        List<String> list = new ArrayList<>();
        ConfigurationSection section = ranksManager.getConfigurationSection(path);
        if (section != null) {
            list.addAll(section.getKeys(false));
        }
        return list;
    }

    public static Integer getValueAfterKey(String key) {
        boolean found = false;
        for (Map.Entry<String, Double> entry : ranks.entrySet()) {
            if (found) {
                return entry.getValue().intValue();
            }
            if (entry.getKey().equals(key)) {
                found = true;
            }
        }
        return null;
    }

    public static void updateRankGroup(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            String rank = getPlayerRank(player.getName());

            for (String group : ranksManager.getConfigurationSection("ranks").getKeys(false)) {
                if (player.hasPermission("group." + group) && !Objects.equals(group, rank)) {
                    user.data().remove(Node.builder("group." + group).build());
                }
            }

            if (!player.hasPermission("group." + rank)) {
                user.data().add(Node.builder("group." + rank).build());
            }

            luckPerms.getUserManager().saveUser(user);
        }

    }

    public static String getPlayerIP(Player player) {
        return Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
    }

    public static int getPlayerRanking(String playerName) {
        File file = new File(plugin.getDataFolder(), "points.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, Double> playerScores = new HashMap<>();
        for (String player : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
            double score = config.getDouble("players." + player);
            playerScores.put(player, score);
        }

        playerScores.putIfAbsent(playerName, 0.0);

        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        for (int i = 0; i < sortedScores.size(); i++) {
            if (sortedScores.get(i).getKey().equals(playerName)) {
                return i + 1;
            }
        }

        return -1;
    }
    public static Map.Entry<String, Double> getPlayerAtRank(int rank) {
        File file = new File(plugin.getDataFolder(), "points.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, Double> playerScores = new HashMap<>();
        for (String player : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
            double score = config.getDouble("players." + player);
            playerScores.put(player, score);
        }

        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        if (rank > 0 && rank <= sortedScores.size()) {
            return sortedScores.get(rank - 1);
        }

        return null;
    }

}
