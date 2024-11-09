package com.softepen.sPvP.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RankTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> COMMANDS = new ArrayList<>();

        if (args.length == 1) {
            if (commandSender.hasPermission("spvp.commands.rank.others") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    COMMANDS.add(player.getName());
                }

                if (commandSender.hasPermission("spvp.commands.rank.set") || commandSender.hasPermission("spvp.commands.rank.*") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
                    COMMANDS.add("set");
                }

                if (commandSender.hasPermission("spvp.commands.rank.add") || commandSender.hasPermission("spvp.commands.rank.*") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
                    COMMANDS.add("add");
                }

                if (commandSender.hasPermission("spvp.commands.rank.remove") || commandSender.hasPermission("spvp.commands.rank.*") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
                    COMMANDS.add("remove");
                }

                StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
            }
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "set")) return null;
            if (Objects.equals(args[0], "add")) return null;
            if (Objects.equals(args[0], "remove")) return null;
        }

        return completions;
    }
}
