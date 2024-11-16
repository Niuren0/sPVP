package com.softepen.sPvP.commands.freeze;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.sPvP.messagesManager;

public class FreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.freeze") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (args.length < 1) commandSender.sendMessage(messagesManager.getPrefixString("cantFindPlayer"));
            else {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) commandSender.sendMessage(messagesManager.getPrefixString("cantFindPlayer"));
                else {
                    if (frozenPlayers.containsKey(player)) commandSender.sendMessage(messagesManager.getPrefixString("alreadyFrozen").replace("{player}", player.getName()));
                    else {
                        if (player == commandSender) commandSender.sendMessage(messagesManager.getPrefixString("cantFreezeYourself"));
                        else {
                            frozenPlayers.put(player, commandSender);
                            player.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", commandSender.getName()));
                            commandSender.sendMessage(messagesManager.getPrefixString("frozen").replace("{player}", player.getName()));
                            player.sendTitle(messagesManager.getPrefixString("frozenTitle").replace("{staff}", commandSender.getName()), "", 10, 40, 10);
                        }
                    }
                }
            }
        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));

        return true;
    }
}
