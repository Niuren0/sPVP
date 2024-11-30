package com.softepen.sPvP.commands.pvpCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.sPvP.messagesManager;

public class Add implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.add") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (args.length < 4) {
                commandSender.sendMessage(messagesManager.getPrefixString("invalidArguments"));
            } else {
                String type = args[1];
                if (!Objects.equals(type, "kill") || !Objects.equals(type, "death")) {
                    commandSender.sendMessage(messagesManager.getPrefixString("invalidArguments"));
                    return true;
                } else {
                    String playerName = args[2];
                    try {
                        int amount = Integer.parseInt(args[3]);
                        Player player = Bukkit.getPlayer(args[0]);

                        if (player == null) {
                            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                            playerData.set(type + "s", playerData.getInt(type + "s") + amount);

                            try {
                                playerData.save(playerFile);
                            } catch (IOException e) {
                                plugin.getLogger().severe("An error occurred on set command: " + e);
                            }
                        } else {
                            if (Objects.equals(type, "kill")) {
                                kills.put(player, kills.get(player) + amount);
                            } else if (Objects.equals(type, "death")) {
                                deaths.put(player, deaths.get(player) + amount);
                            }
                        }
                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(messagesManager.getPrefixString("invalidArguments"));
                        return true;
                    }
                }
            }
        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));
        return true;
    }
}
