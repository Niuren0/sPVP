package com.softepen.sPvP.commands;

import com.softepen.sPvP.commands.pvpCommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class spvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 0) {
            if (Objects.equals(args[0], "reload")) {
                return new Reload().onCommand(commandSender, command, s, args);
            }
            if (Objects.equals(args[0], "set")) {
                return new Set().onCommand(commandSender, command, s, args);
            }
            if (Objects.equals(args[0], "add")) {
                return new Add().onCommand(commandSender, command, s, args);
            }
            if (Objects.equals(args[0], "remove")) {
                return new Remove().onCommand(commandSender, command, s, args);
            }
        }

        return new Settings().onCommand(commandSender, command, s, args);
    }
}
