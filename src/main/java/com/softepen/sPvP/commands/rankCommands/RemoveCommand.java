package com.softepen.sPvP.commands.rankCommands;

import com.softepen.sPvP.managers.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.messagesManager;
import static com.softepen.sPvP.utils.updateRankGroup;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.rank.remove") || commandSender.hasPermission("spvp.commands.rank.*") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (args.length < 3) {
                commandSender.sendMessage(messagesManager.getPrefixString("invalidArguments"));
            } else {
                new RankManager(args[1]).removePoints(Double.parseDouble(args[2]));
                double newPoints = new RankManager(args[1]).getPoints();
                if (newPoints < 0) new RankManager(args[1]).setPoints(0);

                commandSender.sendMessage(messagesManager.getPrefixString("pointsRemoved")
                        .replace("{player}", args[1])
                        .replace("{points}", args[2])
                );

                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) updateRankGroup(player);
            }
        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));

        return true;
    }
}
