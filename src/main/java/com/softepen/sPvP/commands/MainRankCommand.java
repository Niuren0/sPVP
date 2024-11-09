package com.softepen.sPvP.commands;

import com.softepen.sPvP.commands.rankCommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length <= 1) return new RankCommand().onCommand(commandSender, command, s, args);
        else {
            if (Objects.equals(args[0], "set")) {
                return new SetCommand().onCommand(commandSender, command, s, args);
            } else if (Objects.equals(args[0], "add")) {
                return new AddCommand().onCommand(commandSender, command, s, args);
            } else if (Objects.equals(args[0], "remove")) {
                return new RemoveCommand().onCommand(commandSender, command, s, args);
            }
        }

        return new RankCommand().onCommand(commandSender, command, s, args);
    }
}
