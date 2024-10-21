package com.softepen.sPvP.commands.pvpCommands;

import com.softepen.sPvP.menus.SettingsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.messagesManager;

public class Settings implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(messagesManager.getPrefixString("onlyPlayers"));
            return true;
        }

        new SettingsMenu(player);

        return true;
    }
}
