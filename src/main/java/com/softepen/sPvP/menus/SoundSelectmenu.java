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
import static com.softepen.sPvP.sPvP.plugin;

public class SoundSelectmenu {
    public SoundSelectmenu(Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("soundSelectMenu.title")))
                .rows(configManager.getInt("soundSelectMenu.row"))
                .create();

        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);
        GuiItem guiItem;
        int i = 0;

        for (String sound: soundsManager.getKeys(false)) {
            if (Objects.equals(settings.getComboSound(), sound)) {
                guiItem = ItemBuilder.from(getItemStack("selected", sound, null)).asGuiItem();
            } else {
                guiItem = ItemBuilder.from(getItemStack("unselected", sound, null)).asGuiItem(event -> {
                    File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

                    playerData.set("comboSound", Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName());

                    try {
                        playerData.save(playerFile);
                    } catch (IOException e) {
                        plugin.getLogger().severe("An error occurred when saving player data file: " + e);
                    }

                    new SettingsMenu(player);
                });
            }
            gui.setItem(i, guiItem);
            i++;
        }

        GuiItem soundItem = ItemBuilder.from(getItemStack("sound", null, player)).asGuiItem(event -> {
            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            PlayerSettings settings2 = PlayerSettingsManager.getPlayerSettings(player);

            playerData.set("sound", !settings2.getSound());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            gui.updateItem(configManager.getInt("soundSelectMenu.sound.slot"), new ItemStack(getItemStack("sound", null, player)));
        });
        gui.setItem(configManager.getInt("soundSelectMenu.sound.slot"), soundItem);

        GuiItem backItem = ItemBuilder.from(getItemStack("back", null, null)).asGuiItem(event -> new SettingsMenu(player));
        gui.setItem(configManager.getInt("soundSelectMenu.back.slot"), backItem);

        if (configManager.getBoolean("soundSelectMenu.filler.enable")) {
            gui.getFiller().fill(ItemBuilder.from(getItemStack("filler", null , null)).asGuiItem());
        }
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }

    private ItemStack getItemStack(String s,  String name, Player player) {
        Material material;
        String itemName;
        List<String> itemLore;

        if (Objects.equals(s, "sound")) {
            String state = PlayerSettingsManager.getPlayerSettings(player).getSound() ? "enabled_state" : "disabled_state";
            material = Material.valueOf(configManager.getString("soundSelectMenu." + s + "." + state + ".item"));
            itemName = configManager.getString("soundSelectMenu." + s + "." + state + ".title");
            itemLore = configManager.getStringList("soundSelectMenu." + s + "." + state + ".lore");
        } else {
            if (Objects.equals(s, "back") || Objects.equals(s, "filler")) {
                itemName = configManager.getString("soundSelectMenu." + s + ".title");
            } else itemName = name;
            material = Material.valueOf(configManager.getString("soundSelectMenu." + s + ".item"));
            itemLore = configManager.getStringList("soundSelectMenu." + s + ".lore");
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (itemName != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
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
