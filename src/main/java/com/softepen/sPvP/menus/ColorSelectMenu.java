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
import java.util.*;

import static com.softepen.sPvP.sPvP.configManager;
import static com.softepen.sPvP.sPvP.plugin;

public class ColorSelectMenu {
    public ColorSelectMenu(Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("colorSelectMenu.title")))
                .rows(configManager.getInt("colorSelectMenu.row"))
                .create();

        List<String> colors = Arrays.asList(
                "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE",
                "YELLOW", "LIME", "PINK", "GRAY",
                "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE",
                "BROWN", "GREEN", "RED", "BLACK");

        String item = configManager.getString("settingsMenu.color.item").toUpperCase();
        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);
        int i = 0;
        GuiItem colorItem;

        for (String color: colors) {
            Material material = Material.valueOf(color + "_" + item);

            if (Material.valueOf(settings.getHealthIndicatorColor()) == material) {
                colorItem = ItemBuilder.from(getItemStack(material, "selected")).asGuiItem();
            } else {
                colorItem = ItemBuilder.from(getItemStack(material, "unselected")).asGuiItem(event -> {
                    File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                    playerData.set("healthIndicatorColor", material.name());

                    try {
                        playerData.save(playerFile);
                    } catch (IOException e) {
                        plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                    }

                    new SettingsMenu(player);
                });
            }

            gui.setItem(i, colorItem);
            i++;
        }

        GuiItem backItem = ItemBuilder.from(getItemStack(null, "back")).asGuiItem(event -> {
            event.setCancelled(true);
            new SettingsMenu(player);
        });
        gui.setItem(configManager.getInt("colorSelectMenu.back.slot"), backItem);

        if (configManager.getBoolean("colorSelectMenu.filler.enable")) {
            gui.getFiller().fill(ItemBuilder.from(getItemStack(null, "filler")).asGuiItem());
        }
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }

    private ItemStack getItemStack(Material material, String s) {
        List<String> itemLore;
        String itemName = null;

        if (Objects.equals(s, "back") || Objects.equals(s, "filler")) {
            material = Material.valueOf(configManager.getString("colorSelectMenu." + s + ".item"));
            itemName = configManager.getString("colorSelectMenu." + s + ".title");
        }
        itemLore = configManager.getStringList("colorSelectMenu." + s + ".lore");

        if (itemLore != null) {
            ListIterator<String> iterator = itemLore.listIterator();
            while (iterator.hasNext()) {
                String lore = iterator.next();
                lore = ChatColor.translateAlternateColorCodes('&', lore);
                iterator.set(lore);
            }
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemName);
            if (itemLore != null) meta.setLore(itemLore);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
