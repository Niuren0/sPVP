package com.softepen.sPvP;

import com.sk89q.worldguard.WorldGuard;
import com.softepen.sPvP.commands.MainCommand;
import com.softepen.sPvP.commands.ProfileCommand;
import com.softepen.sPvP.commands.freeze.FreezeCommand;
import com.softepen.sPvP.commands.freeze.UnfreezeCommand;
import com.softepen.sPvP.commands.spvpTabComplete;
import com.softepen.sPvP.events.*;
import com.softepen.sPvP.listeners.papi;
import com.softepen.sPvP.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class sPvP extends JavaPlugin {
    public static sPvP plugin;
    public static FileManager configManager;
    public static FileManager soundsManager;
    public static FileManager messagesManager;
    public static File logFile;
    public static HashMap<Player, Integer> criticalHitCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitLastCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitComboRecord = new HashMap<>();
    public static HashMap<Player, Integer> killSeries = new HashMap<>();
    public static HashMap<Player, Integer> killSeriesRecord = new HashMap<>();
    public static HashMap<Player, Integer> kills = new HashMap<>();
    public static HashMap<Player, Integer> deaths = new HashMap<>();
    public static HashMap<Player, CommandSender> frozens = new HashMap<>();
    public static boolean wgExpansion;
    public static WorldGuard WGPlugin;

    @Override
    public void onEnable() {
        plugin = this;

        configManager = new FileManager("config.yml");
        configManager.loadConfig();

        soundsManager = new FileManager("sounds.yml");
        soundsManager.loadConfig();

        messagesManager = new FileManager("lang/en.yml");
        messagesManager.loadConfig();
        messagesManager = new FileManager("lang/tr.yml");
        messagesManager.loadConfig();
        messagesManager = new FileManager("lang/" + configManager.getString("language") + ".yml");
        messagesManager.loadConfig();

        if (configManager.getBoolean("log.enable")) {
            logFile = new File(plugin.getDataFolder(), "logs/deaths.log");

            try {
                File logDir = logFile.getParentFile();
                if (!logDir.exists() && !logDir.mkdir()) {
                    getLogger().severe("Log directory couldn't be created.");
                }

                if (!logFile.exists() && !logFile.createNewFile()) {
                    getLogger().severe("Log file couldn't be created.");
                }

            } catch (IOException e) {
                getLogger().severe("Log file creation failed: " + e.getMessage());
            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new papi().register();
            getLogger().info("sPvP PlaceholderAPI expansion enabled.");
        } else {
            getLogger().warning("PlaceholderAPI plugin not found. Expansion disabled.");
        }

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WGPlugin = WorldGuard.getInstance();
            wgExpansion = true;
            getLogger().info("sPvP WorldGuard expansion enabled.");
        } else {
            wgExpansion = false;
            getLogger().warning("WorldGuard plugin not found. Expansion disabled.");
        }

        Objects.requireNonNull(getCommand("spvp")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("profile")).setExecutor(new ProfileCommand());
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new FreezeCommand());
        Objects.requireNonNull(getCommand("unfreeze")).setExecutor(new UnfreezeCommand());

        Objects.requireNonNull(getCommand("spvp")).setTabCompleter(new spvpTabComplete());


        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBreak(), this);
        getServer().getPluginManager().registerEvents(new OnClick(), this);
        getServer().getPluginManager().registerEvents(new OnCommand(), this);
        getServer().getPluginManager().registerEvents(new OnDrop(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryOpen(), this);
        getServer().getPluginManager().registerEvents(new OnMove(), this);
        getServer().getPluginManager().registerEvents(new OnPickup(), this);
        getServer().getPluginManager().registerEvents(new OnPlace(), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(), this);

        List<String> lines = List.of(
                "&c-=-=-=-=-=-=-=-=-= &6sPvP Plugin &c-=-=-=-=-=-=-=-=-=",
                "&aThe sPvP plugin has been successfully enabled!",
                "&aFor support or questions, visit our website: &6www.softepen.com",
                "&c-=-=-=-=-=-=-=-=-=-= &6Softepen &c-=-=-=-=-=-=-=-=-=-="
        );
        for (String line : lines) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    @Override
    public void onDisable() {
        List<String> lines = List.of(
                "&c-=-=-=-=-=-=-= &6sPvP Plugin &c-=-=-=-=-=-=",
                "&aAn error occurred in the plugin.",
                "&aFor support, visit www.Softepen.com",
                "&c-=-=-=-=-=-=-= &6Softepen &c-=-=-=-=-=-=-=");
        for (String line : lines) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}
