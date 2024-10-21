package com.softepen.sPvP;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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

    public static Sound getComboSound(int combo, Sound defaultSound) {
        String path = configManager.getString("sounds.critical_hit");
        String soundName = soundsManager.getString(path + "." + combo + ".sound");
        try {
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultSound;
        }
    }

    public static float getComboSoundPitch(int combo) {
        String path = configManager.getString("sounds.critical_hit");
        float soundPitch;
        soundPitch = (float) soundsManager.getDouble(path + "." + combo + ".pitch");
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
}
