package com.softepen.sPvP;

import com.softepen.sPvP.commands.MainCommand;
import com.softepen.sPvP.commands.spvpTabComplete;
import com.softepen.sPvP.events.DamageEvent;
import com.softepen.sPvP.events.DeathEvent;
import com.softepen.sPvP.events.JoinEvent;
import com.softepen.sPvP.events.QuitEvent;
import com.softepen.sPvP.listeners.papi;
import com.softepen.sPvP.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class sPvP extends JavaPlugin {
    public static sPvP plugin;
    public static FileManager configManager;
    public static FileManager soundsManager;
    public static FileManager messagesManager;
    public static HashMap<Player, Integer> criticalHitCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitLastCombo = new HashMap<>();
    public static HashMap<Player, Integer> criticalHitComboRecord = new HashMap<>();
    public static HashMap<Player, Integer> killSeries = new HashMap<>();
    public static HashMap<Player, Integer> killSeriesRecord = new HashMap<>();
    public static HashMap<Player, Integer> kills = new HashMap<>();
    public static HashMap<Player, Integer> deaths = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        configManager = new FileManager("config.yml");
        configManager.loadConfig();

        soundsManager = new FileManager("sounds.yml");
        soundsManager.loadConfig();

        messagesManager = new FileManager("lang/en.yml");
        messagesManager.loadConfig();
//        messagesManager = new FileManager("lang/tr.yml");
//        messagesManager.loadConfig();
        messagesManager = new FileManager("lang/" + configManager.getString("language") + ".yml");
        messagesManager.loadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new papi().register();
            getLogger().info("sPvP PlaceholderAPI expansion enabled.");
        } else {
            getLogger().warning("PlaceholderAPI plugin not found. sPvp PlaceholderAPI expansion disabled.");
        }

        Objects.requireNonNull(getCommand("spvp")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("spvp")).setTabCompleter(new spvpTabComplete());

        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

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
