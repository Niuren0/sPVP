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

public class KillMessagesSelector {
    public KillMessagesSelector(Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("killMessagesMenu.title")))
                .rows(configManager.getInt("killMessagesMenu.row"))
                .create();

        int i = 0;
        GuiItem guiItem;
        String message;
        for (String key : messagesManager.getConfigurationSection("specialKillMessages").getKeys(false)) {
            message = messagesManager.getString("specialKillMessages." + key)
                    .replace("{victim}", playerName)
                    .replace("{attacker}", playerName)
                    .replace("{killer}", playerName);

            if (player.hasPermission("spvp.messages." + key) || player.hasPermission("spvp.messages.*") || player.hasPermission("spvp.*")) {
                PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);
                if (Objects.equals(settings.getKillMessage(), key)) {
                    guiItem = ItemBuilder.from(getItemStack("selected", message)).asGuiItem(event -> {
                        File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                        playerData.set("killMessage", null);

                        try {
                            playerData.save(playerFile);
                        } catch (IOException e) {
                            plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                        }

                        new SettingsMenu(player);
                    });
                } else {
                    guiItem = ItemBuilder.from(getItemStack("hasPerm", message)).asGuiItem(event -> {
                        File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                        playerData.set("killMessage", key);

                        try {
                            playerData.save(playerFile);
                        } catch (IOException e) {
                            plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                        }

                        new SettingsMenu(player);

                    });
                }
            } else {
                guiItem = ItemBuilder.from(getItemStack("hasNotPerm", message)).asGuiItem();
            }

            gui.setItem(i, guiItem);
            i++;
        }

        GuiItem backItem = ItemBuilder.from(getItemStack("back", "")).asGuiItem(event -> new SettingsMenu(player));
        gui.setItem(configManager.getInt("killMessagesMenu.back.slot"), backItem);

        if (configManager.getBoolean("killMessagesMenu.filler.enable")) {
            for (String key : configManager.getConfigurationSection("killMessagesMenu.filler.items").getKeys(false)) {
                List<Integer> slots = configManager.getIntegerList("killMessagesMenu.filler.items." + key + ".slots");
                GuiItem fillerItem = ItemBuilder.from(getItemStack("killMessagesMenu.filler.items." + key, null)).asGuiItem();
                for (Integer slot : slots) {
                    gui.setItem(slot, fillerItem);
                }
            }
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }

    private ItemStack getItemStack(String s, String message) {
        Material material;
        String itemName;
        List<String> itemLore;

        if (s.startsWith("killMessagesMenu.filler.items.")) {
            material = Material.valueOf(configManager.getString(s + ".item"));
            itemName = configManager.getString(s + ".title");
            itemLore = configManager.getStringList(s + ".lore");
        } else {
            material = Material.valueOf(configManager.getString("killMessagesMenu." + s + ".item"));
            itemName = configManager.getString("killMessagesMenu." + s + ".title").replace("{message}", message);
            itemLore = configManager.getStringList("killMessagesMenu." + s + ".lore");
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
