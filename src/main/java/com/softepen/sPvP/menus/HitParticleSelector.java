package com.softepen.sPvP.menus;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.sPvP.playerSettings;

public class HitParticleSelector {
    public HitParticleSelector(Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
            .title(Component.text(configManager.getString("particleSelectMenu.title")))
            .rows(configManager.getInt("particleSelectMenu.row"))
            .create();

        int i = 0;
        GuiItem guiItem;
        for (String key : configManager.getConfigurationSection("particles").getKeys(false)) {
            String name = configManager.getString("particles." + key + ".name");
            boolean menu = configManager.getBoolean("particles." + key + ".menu");

            if (menu) {

                if (player.hasPermission("spvp.particles." + key) || player.hasPermission("spvp.particles.*") || player.hasPermission("spvp.*")) {
                    PlayerSettings settings = playerSettings.get(player);
                    if (Objects.equals(settings.getParticle(), key)) {
                        guiItem = ItemBuilder.from(getItemStack("selected", name)).asGuiItem(event -> {
                            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                            playerData.set("particle", null);

                            try {
                                playerData.save(playerFile);
                            } catch (IOException e) {
                                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                            }

                            playerSettings.put(player, PlayerSettingsManager.getPlayerSettings(player));

                            new SettingsMenu(player);
                        });
                    } else {
                        guiItem = ItemBuilder.from(getItemStack("hasPerm", name)).asGuiItem(event -> {
                            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                            playerData.set("particle", key);

                            try {
                                playerData.save(playerFile);
                            } catch (IOException e) {
                                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                            }

                            playerSettings.put(player, PlayerSettingsManager.getPlayerSettings(player));

                            new SettingsMenu(player);

                        });
                    }
                } else {
                    guiItem = ItemBuilder.from(getItemStack("hasNotPerm", name)).asGuiItem();
                }

                gui.setItem(i, guiItem);
                i++;
            }
        }

        GuiItem backItem = ItemBuilder.from(getItemStack("back", "")).asGuiItem(event -> new SettingsMenu(player));
        gui.setItem(configManager.getInt("particleSelectMenu.back.slot"), backItem);

        if (configManager.getBoolean("particleSelectMenu.filler.enable")) {
            for (String key : configManager.getConfigurationSection("particleSelectMenu.filler.items").getKeys(false)) {
                List<Integer> slots = configManager.getIntegerList("particleSelectMenu.filler.items." + key + ".slots");
                GuiItem fillerItem = ItemBuilder.from(getItemStack("particleSelectMenu.filler.items." + key, null)).asGuiItem();
                for (Integer slot : slots) {
                    gui.setItem(slot, fillerItem);
                }
            }
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setCloseGuiAction(event -> playerSettings.put(player, PlayerSettingsManager.getPlayerSettings(player)));
        gui.open(player);
    }

    private ItemStack getItemStack(String s, String name) {
        Material material;
        String itemName;
        List<String> itemLore;

        if (s.startsWith("particleSelectMenu.filler.items.")) {
            material = Material.valueOf(configManager.getString(s + ".item"));
            itemName = configManager.getString(s + ".title");
            itemLore = configManager.getStringList(s + ".lore");
        } else {
            material = Material.valueOf(configManager.getString("particleSelectMenu." + s + ".item"));
            itemName = configManager.getString("particleSelectMenu." + s + ".title").replace("{name}", name);
            itemLore = configManager.getStringList("particleSelectMenu." + s + ".lore");
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            if (itemLore != null) {
                List<String> coloredLore = new ArrayList<>();
                for (String lore : itemLore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                meta.setLore(coloredLore);
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
