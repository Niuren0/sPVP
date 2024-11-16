package com.softepen.sPvP.commands.freeze;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.frozenPlayers;
import static com.softepen.sPvP.sPvP.messagesManager;

public class UnfreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.unfreeze") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (args.length < 1) commandSender.sendMessage(messagesManager.getPrefixString("cantFindPlayer"));
            else {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) commandSender.sendMessage(messagesManager.getPrefixString("cantFindPlayer"));
                else {
                    if (!frozenPlayers.containsKey(player)) commandSender.sendMessage(messagesManager.getPrefixString("notFrozen").replace("{player}", player.getName()));
                    else {
                        frozenPlayers.remove(player);
                        player.sendMessage(messagesManager.getPrefixString("unfrozenBy").replace("{staff}", commandSender.getName()));
                        commandSender.sendMessage(messagesManager.getPrefixString("unfrozen").replace("{player}", player.getName()));
                    }
                }
            }
        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));

        return true;
    }
}