package com.softepen.sPvP.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(messagesManager.getPrefixString("onlyPlayers"));
            return true;
        }

        String soundName = configManager.getString("sounds.critical_hit");

        for (String key : soundsManager.getConfigurationSection(soundName).getKeys(false)) {
            String sound = soundsManager.getString(soundName + "." + key + ".sound");
            float pitch = (float) soundsManager.getDouble(soundName + "." + key + ".pitch");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, pitch);

            try {
                Thread.sleep(200); // 200ms gecikme
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
