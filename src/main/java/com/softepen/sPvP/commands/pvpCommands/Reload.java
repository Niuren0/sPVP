package com.softepen.sPvP.commands.pvpCommands;

import com.softepen.sPvP.managers.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        configManager.reloadConfig();
        soundsManager.reloadConfig();
        messagesManager = new FileManager("lang/" + configManager.getString("language") + ".yml");
        messagesManager.loadConfig();

        commandSender.sendMessage(messagesManager.getPrefixString("reloaded"));
        return true;
    }
}
