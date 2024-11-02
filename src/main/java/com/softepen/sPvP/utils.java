package com.softepen.sPvP;

import com.softepen.sPvP.managers.PlayerSettingsManager;
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
            plugin.getLogger().warning("Error at " + player.getName() + "'s getComboSound function :" + e);
            return null;
        }
    }

    public static float getComboSoundPitch(int combo, Player player) {
        String path = PlayerSettingsManager.getPlayerSettings(player).getComboSound();
        float soundPitch = (float) soundsManager.getDouble(path + "." + combo + ".pitch", 1);
        try {
            return soundPitch;
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
            plugin.getLogger().severe("Log dosyasına yazılamadı: " + e.getMessage());
        }
    }
}
