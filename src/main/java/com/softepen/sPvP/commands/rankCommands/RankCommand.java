package com.softepen.sPvP.commands.rankCommands;

import com.softepen.sPvP.managers.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.softepen.sPvP.sPvP.messagesManager;
import static com.softepen.sPvP.utils.*;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        String name;

        if (args.length == 0) {
            if ((commandSender instanceof Player)) {
                if (commandSender.hasPermission("spvp.commands.rank") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
                    name = commandSender.getName();
                } else {
                    commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));
                    return true;
                }
            } else {
                commandSender.sendMessage(messagesManager.getPrefixString("onlyPlayers"));
                return true;
            }
        } else {
            if (commandSender.hasPermission("spvp.commands.rank.others") || commandSender.hasPermission("spvp.commands.*") ||commandSender.hasPermission("spvp.*")) {
                name = args[0];
            } else {
                commandSender.sendMessage(messagesManager.getPrefixString("noPerm"));
                return true;
            }
        }

        Player targetPlayer = Bukkit.getPlayer(name);
        if (targetPlayer != null) updateRankGroup(targetPlayer);

        double playerPoints = new RankManager(name).getPoints();
        String role = getPlayerRank(name);
        Integer nextRolePoints = getValueAfterKey(role);
        double remainingPoints = nextRolePoints != null ? nextRolePoints - playerPoints : 0;

        commandSender.sendMessage(messagesManager.getPrefixString("rank")
                .replace("{player}", name)
                .replace("{rank}", role)
                .replace("{points}", String.format("%.2f", playerPoints))
                .replace("{remainingPoints}", String.format("%.2f", remainingPoints))
                .replace("{ranking}", String.valueOf(getPlayerRanking(name)))
        );

        return true;
    }
}
