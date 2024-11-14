package com.softepen.sPvP.commands.rankCommands;

import com.softepen.sPvP.managers.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.logMessage;
import static com.softepen.sPvP.utils.updateRankGroup;

public class AddCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.hasPermission("spvp.commands.rank.add") || commandSender.hasPermission("spvp.commands.rank.*") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
            if (args.length < 3) {
                commandSender.sendMessage(messagesManager.getPrefixString("invalidArguments"));
            } else {
                double beforePoints = new RankManager(args[1]).getPoints();

                new RankManager(args[1]).addPoints(Double.parseDouble(args[2]));

                double afterPoints = new RankManager(args[1]).getPoints();

                commandSender.sendMessage(messagesManager.getPrefixString("pointsAdded")
                        .replace("{player}", args[1])
                        .replace("{points}", args[2])
                );

                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) updateRankGroup(player);

                // COMMAND LOG
                if (configManager.getBoolean("log.command.enable")) {
                    String message = configManager.getString("log.command.format")
                            .replace("{sender}", commandSender.getName())
                            .replace("{command}", "/rank add " + args[1] + " " + args[2])
                            .replace("{beforePoints}", String.valueOf(beforePoints))
                            .replace("{afterPoints}", String.valueOf(afterPoints));

                    logMessage(message, commandLogFile);
                }
            }
        } else commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));

        return true;
    }
}
