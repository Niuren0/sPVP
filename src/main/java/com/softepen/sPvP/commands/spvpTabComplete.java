package com.softepen.sPvP.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class spvpTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> COMMANDS = new ArrayList<>();

        if (commandSender.isOp()) {
            if (args.length == 1) {
                COMMANDS.add("reload");
                StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
            }
        } else completions = null;

        return completions;
    }
}
