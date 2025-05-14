package com.softepen.sPvP.events;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import com.softepen.sPvP.managers.RankManager;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.*;

public class DeathEventPVP implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            String victimName = victim.getName();
            String killerName = killer.getName();

            // RANK POINTS
            double earnedPoints = 0, lostPoints = 0;
            if (configManager.getBoolean("rank.enabled")) {
                if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                    if (!isPlayerInDisabledRegion(killer, "rank") ||
                            !isPlayerInDisabledRegion(victim, "rank") ||
                            !isPlayerInDisabledWorld(killer, "rank") ||
                            !isPlayerInDisabledWorld(victim, "rank")) {

                        RankManager victimSettings = new RankManager(victimName);
                        String victimRank = getPlayerRank(victim.getName());

                        String killerGroup = getPlayerGroupAsync(killer.getUniqueId());
                        RankManager killerSettings = new RankManager(killerName);

                        double min = ranksManager.getDouble("ranks." + victimRank + ".min", 1);
                        double max = ranksManager.getDouble("ranks." + victimRank + ".max", 3);

                        Random random = new Random();

                        lostPoints = min + (max - min) * random.nextDouble();
                        earnedPoints = lostPoints + (lostPoints * ranksManager.getDouble("earnedPoints." + killerGroup, ranksManager.getDouble("earnedPoints.default")) / 100);

                        lostPoints = Math.floor(lostPoints * 100) / 100;
                        earnedPoints = Math.floor(earnedPoints * 100) / 100;

//                        updateRankGroup(victim);
//                        updateRankGroup(killer);

                        if (configManager.getBoolean("ipPrevent.rank")) {
                            if (Objects.equals(getPlayerIP(killer), getPlayerIP(victim))) {
                                earnedPoints = 0;
                                lostPoints = 0;
                            }
                        }

                        if (isInCooldown(killerName, victimName)) {
                            earnedPoints = 0;
                            lostPoints = 0;
                        }

                        if (victimSettings.getPoints() < lostPoints) victimSettings.setPoints(0.0);
                        else victimSettings.removePoints(lostPoints);
                        killerSettings.addPoints(earnedPoints);
                    }
                } else {
                    plugin.getLogger().warning("LuckPerms needed for advanced rank system!");
                }
            }

            // STREAK KILL DEATH SAVES, KILL MESSAGES
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            String broadcastMessage = messagesManager.getPrefixString("deathMessage");

            if (!isPlayerInDisabledRegion(killer, "death") ||
                    !isPlayerInDisabledRegion(victim, "death") ||
                    !isPlayerInDisabledWorld(killer, "death") ||
                    !isPlayerInDisabledWorld(victim, "death")) {

                if (!isInCooldown(killerName, victimName)) {
                    killSeries.remove(victim);
                    deaths.put(victim, deaths.getOrDefault(victim, 0) + 1);

                    boolean ipPreventStreakBoolean = false;
                    if (configManager.getBoolean("ipPrevent.streak")) {
                        if (Objects.equals(getPlayerIP(killer), getPlayerIP(victim))) {
                            ipPreventStreakBoolean = true;
                        }
                    }

                    if (!ipPreventStreakBoolean) {
                        killSeries.put(killer, killSeries.getOrDefault(killer, 0) + 1);
                        kills.put(killer, kills.getOrDefault(killer, 0) + 1);

                        int currentSeries = killSeries.get(killer);

                        if (killSeriesRecord.getOrDefault(killer, 0) < currentSeries) killSeriesRecord.put(killer, currentSeries);

                        if (configManager.getBoolean("streakAnnouncements")) {
                            String streakMessage = getStreakMessages(currentSeries);
                            if (streakMessage != null) {
                                broadcastMessage = streakMessage;
                            }
                        }
                    }

                    if (configManager.getBoolean("specialKillMessages")) {
                        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(killer);
                        String perm = settings.getKillMessage();
                        if (perm != null) {
                            if (killer.hasPermission("spvp.messages." + perm)) {
                                String killMessage = getSpecialKillMessage(perm, killer, victim);
                                if (killMessage != null) broadcastMessage = killMessage;
                                else plugin.getLogger().warning("Special kill messages is null: " + perm);
                            }
                        }
                    }
                }

                boolean ipPreventKillBoolean = false;
                if (configManager.getBoolean("ipPrevent.kill")) {
                    if (Objects.equals(getPlayerIP(killer), getPlayerIP(victim))) {
                        ipPreventKillBoolean = true;
                    }
                }

                if (!ipPreventKillBoolean) {
                    // KILLER SAVE
                    if (!isInCooldown(killerName, victimName)) {
                        File killerFile = new File(plugin.getDataFolder(), "data/" + killerName + ".yml");
                        FileConfiguration killerData = YamlConfiguration.loadConfiguration(killerFile);

                        int killerKills = killerData.getInt("kill." + victimName, 0);
                        killerData.set("kill." + victimName, killerKills + 1);

                        killerData.set("last5kills.5.player", killerData.getString("last5kills.4.player", null));
                        killerData.set("last5kills.5.time", killerData.getString("last5kills.4.time", null));

                        killerData.set("last5kills.4.player", killerData.getString("last5kills.3.player", null));
                        killerData.set("last5kills.4.time", killerData.getString("last5kills.3.time", null));

                        killerData.set("last5kills.3.player", killerData.getString("last5kills.2.player", null));
                        killerData.set("last5kills.3.time", killerData.getString("last5kills.2.time", null));

                        killerData.set("last5kills.2.player", killerData.getString("last5kills.1.player", null));
                        killerData.set("last5kills.2.time", killerData.getString("last5kills.1.time", null));

                        killerData.set("last5kills.1.player", victimName);
                        killerData.set("last5kills.1.time", formattedDateTime);

                        try {
                            killerData.save(killerFile);
                        } catch (IOException e) {
                            plugin.getLogger().severe("An error occurred when saving " + killerName + " killer data file: " + e);
                        }

                        // VICTIM SAVE
                        File victimFile = new File(plugin.getDataFolder(), "data/" + victimName + ".yml");
                        FileConfiguration victimData = YamlConfiguration.loadConfiguration(victimFile);

                        int victimDeaths = victimData.getInt("death." + killerName, 0);
                        victimData.set("death." + killerName, victimDeaths + 1);

                        victimData.set("last5deaths.5.player", victimData.getString("last5deaths.4.player", null));
                        victimData.set("last5deaths.5.time", victimData.getString("last5deaths.4.time", null));

                        victimData.set("last5deaths.4.player", victimData.getString("last5deaths.3.player", null));
                        victimData.set("last5deaths.4.time", victimData.getString("last5deaths.3.time", null));

                        victimData.set("last5deaths.3.player", victimData.getString("last5deaths.2.player", null));
                        victimData.set("last5deaths.3.time", victimData.getString("last5deaths.2.time", null));

                        victimData.set("last5deaths.2.player", victimData.getString("last5deaths.1.player", null));
                        victimData.set("last5deaths.2.time", victimData.getString("last5deaths.1.time", null));

                        victimData.set("last5deaths.1.player", killerName);
                        victimData.set("last5deaths.1.time", formattedDateTime);

                        try {
                            victimData.save(victimFile);
                        } catch (IOException e) {
                            plugin.getLogger().severe("An error occurred when saving " + victimName + " victim data file: " + e);
                        }
                    }
                }
            }

            broadcastMessage = broadcastMessage
                    .replace("{victim}", victim.getDisplayName())
                    .replace("{killer}", killer.getDisplayName())
                    .replace("{attacker}", killer.getDisplayName())
                    .replace("{+points}", String.valueOf(earnedPoints))
                    .replace("{-points}", String.valueOf(lostPoints));

            Bukkit.broadcastMessage(broadcastMessage);

            // DEATH LOG
            if (configManager.getBoolean("log.kill.enable")) {
                String message = configManager.getString("log.kill.format")
                        .replace("{killer}", killer.getName())
                        .replace("{victim}", victim.getName())
                        .replace("{+points}", String.valueOf(earnedPoints))
                        .replace("{-points}", String.valueOf(lostPoints));

                logMessage(message, killLogFile);
            }

            playerSettings.put(killer, PlayerSettingsManager.getPlayerSettings(killer));
            playerSettings.put(victim, PlayerSettingsManager.getPlayerSettings(victim));
        }
    }

    public static String getPlayerGroupAsync(UUID playerUUID) {
        CompletableFuture<String> victimGroupFuture = getPlayerGroup(playerUUID);
        return victimGroupFuture.join();
    }
    public static CompletableFuture<String> getPlayerGroup(UUID playerUUID) {
        return luckPerms.getUserManager().loadUser(playerUUID).thenApply(User::getPrimaryGroup);
    }
}
