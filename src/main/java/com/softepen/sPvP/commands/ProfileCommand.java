package com.softepen.sPvP.commands;

import com.softepen.sPvP.menus.ProfileMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.profile") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (!(commandSender instanceof Player opener)) {
                commandSender.sendMessage(messagesManager.getPrefixString("onlyPlayers"));
                return true;
            }

            if (args.length == 0) {
                new ProfileMenu(opener, opener);
            } else {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    opener.sendMessage(messagesManager.getPrefixString("cantFindPlayer"));
                    return true;
                }

                new ProfileMenu(opener, player);
            }

        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));

        return true;
    }
}
