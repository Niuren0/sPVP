package com.softepen.sPvP;

import com.sk89q.worldguard.WorldGuard;
import com.softepen.sPvP.commands.*;
import com.softepen.sPvP.commands.freeze.FreezeCommand;
import com.softepen.sPvP.commands.freeze.UnfreezeCommand;
import com.softepen.sPvP.events.*;
import com.softepen.sPvP.listeners.papi;
import com.softepen.sPvP.managers.FileManager;
import com.softepen.sPvP.managers.PlayerSettings;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.utils.ranksReload;

public final class sPvP extends JavaPlugin {
    public static sPvP plugin;
    public static FileManager configManager;
    public static FileManager soundsManager;
    public static FileManager messagesManager;
    public static File killLogFile;
    public static File commandLogFile;
    public static FileManager pointsManager;
    public static FileManager ranksManager;
    public static HashMap<Player, Integer> criticalHitCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitLastCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitComboRecord = new HashMap<>();
    public static HashMap<Player, Integer> killSeries = new HashMap<>();
    public static HashMap<Player, Integer> killSeriesRecord = new HashMap<>();
    public static HashMap<Player, Integer> kills = new HashMap<>();
    public static HashMap<Player, Integer> deaths = new HashMap<>();
    public static HashMap<Player, CommandSender> frozenPlayers = new HashMap<>();
    public static HashMap<String, Double> ranks = new HashMap<>();
    public static HashMap<Player, PlayerSettings> playerSettings = new HashMap<>();
    public static boolean wgExpansion;
    public static WorldGuard WGPlugin;
    public static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        plugin = this;

        setupConfigs();
        setupLogFiles();
        setupPlaceholderAPI();
        setupWorldGuard();
        setupLuckPerms();
        registerCommands();
        registerEvents();

        List<String> lines = List.of(
                "&c-=-=-=-=-=-=-=-=-= &6sPvP Plugin &c-=-=-=-=-=-=-=-=-=",
                "&aThe sPvP plugin has been successfully enabled!",
                "&aFor support, visit: &6www.softepen.com",
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
                "&aThe sPvP plugin has been disabled.",
                "&aFor support, visit www.Softepen.com",
                "&c-=-=-=-=-=-=-= &6Softepen &c-=-=-=-=-=-=-=");
        for (String line : lines) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    private void setupConfigs() {
        configManager = new FileManager("config.yml");
        configManager.loadConfig();

        soundsManager = new FileManager("sounds.yml");
        soundsManager.loadConfig();

        messagesManager = new FileManager("lang/" + configManager.getString("language") + ".yml");
        messagesManager.loadConfig();

        pointsManager = new FileManager("points.yml");
        pointsManager.loadConfig();

        ranksManager = new FileManager("ranks.yml");
        ranksManager.loadConfig();
        ranksReload();
    }

    private void setupLogFiles() {
        killLogFile = createLogFile("logs/deaths.log");
        commandLogFile = createLogFile("logs/commands.log");
    }

    private File createLogFile(String path) {
        File file = new File(getDataFolder(), path);
        try {
            File dir = file.getParentFile();
            if (!dir.exists() && !dir.mkdirs()) {
                getLogger().severe("Couldn't create directory for: " + path);
            }

            if (!file.exists() && !file.createNewFile()) {
                getLogger().severe("Couldn't create file: " + path);
            }
        } catch (IOException e) {
            getLogger().severe("Log file creation failed for " + path + ": " + e.getMessage());
        }
        return file;
    }

    private void setupPlaceholderAPI() {
        Plugin papiPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (papiPlugin != null && papiPlugin.isEnabled()) {
            new papi().register();
            getLogger().info("sPvP PlaceholderAPI expansion enabled.");
        } else {
            getLogger().warning("PlaceholderAPI plugin not found or not enabled. Expansion disabled.");
        }
    }

    private void setupWorldGuard() {
        Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (wg != null && wg.isEnabled()) {
            WGPlugin = WorldGuard.getInstance();
            wgExpansion = true;
            getLogger().info("sPvP WorldGuard expansion enabled.");
        } else {
            wgExpansion = false;
            getLogger().warning("WorldGuard plugin not found or not enabled. Expansion disabled.");
        }
    }

    private void setupLuckPerms() {
        Plugin lp = Bukkit.getPluginManager().getPlugin("LuckPerms");
        if (lp != null && lp.isEnabled()) {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("sPvP LuckPerms expansion enabled.");
        } else {
            getLogger().warning("LuckPerms plugin not found or not enabled. Expansion disabled.");
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("spvp")).setExecutor(new spvpCommand());
        Objects.requireNonNull(getCommand("profile")).setExecutor(new ProfileCommand());
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new FreezeCommand());
        Objects.requireNonNull(getCommand("unfreeze")).setExecutor(new UnfreezeCommand());
        Objects.requireNonNull(getCommand("rank")).setExecutor(new MainRankCommand());

        Objects.requireNonNull(getCommand("spvp")).setTabCompleter(new spvpTabComplete());
        Objects.requireNonNull(getCommand("rank")).setTabCompleter(new RankTabComplete());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEventPVP(), this);
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
    }
}
