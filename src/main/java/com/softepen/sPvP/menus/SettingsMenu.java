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

public class SettingsMenu {
    public SettingsMenu (Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("settingsMenu.title")))
                .rows(configManager.getInt("settingsMenu.row"))
                .create();

        GuiItem indicatorItem = ItemBuilder.from(getItemStack(player, "healthIndicator")).asGuiItem(event -> new ColorSelectMenu(player));
        
        GuiItem soundItem = ItemBuilder.from(getItemStack(player, "sound")).asGuiItem(event -> new HitSoundSelectMenu(player));

        GuiItem killMessagesItem = ItemBuilder.from(getItemStack(player, "killMessages")).asGuiItem(event -> new KillMessagesSelector(player));

        GuiItem comboMessagesItem = ItemBuilder.from(getItemStack(player, "comboMessages")).asGuiItem(event -> {
            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            PlayerSettings settings = playerSettings.get(player);

            playerData.set("comboMessages", !settings.getComboMessages());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            playerSettings.put(player, PlayerSettingsManager.getPlayerSettings(player));

            gui.updateItem(configManager.getInt("settingsMenu.comboMessages.slot"), new ItemStack(getItemStack(player, "comboMessages")));
        });

        gui.setItem(configManager.getInt("settingsMenu.sound.slot"), soundItem);
        gui.setItem(configManager.getInt("settingsMenu.killMessages.slot"), killMessagesItem);
        gui.setItem(configManager.getInt("settingsMenu.comboMessages.slot"), comboMessagesItem);
        gui.setItem(configManager.getInt("settingsMenu.healthIndicator.slot"), indicatorItem);

        if (configManager.getBoolean("settingsMenu.filler.enable")) {
            for (String key : configManager.getConfigurationSection("settingsMenu.filler.items").getKeys(false)) {
                List<Integer> slots = configManager.getIntegerList("settingsMenu.filler.items." + key + ".slots");
                GuiItem fillerItem = ItemBuilder.from(getItemStack(player, "settingsMenu.filler.items." + key)).asGuiItem();
                for (Integer slot : slots) {
                    gui.setItem(slot, fillerItem);
                }
            }
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setCloseGuiAction(event -> playerSettings.put(player, PlayerSettingsManager.getPlayerSettings(player)));

        gui.open(player);
    }

    private ItemStack getItemStack(Player player, String item) {
        Material material;
        String state = null, itemName;
        List<String> itemLore;

        if (Objects.equals(item, "comboMessages")) {
            PlayerSettings settings = playerSettings.get(player);
            state = settings.getComboMessages() ? "enabled_state" : "disabled_state";
        }

        if (state != null) {
            material = Material.valueOf(configManager.getString("settingsMenu." + item + "." + state + ".item"));
            itemName = configManager.getString("settingsMenu." + item + "." + state + ".title");
            itemLore = configManager.getStringList("settingsMenu." + item + "." + state + ".lore");
        } else {
            if (item.startsWith("settingsMenu.filler.items.")) {
                material = Material.valueOf(configManager.getString(item + ".item"));
                itemName = configManager.getString(item + ".title");
                itemLore = configManager.getStringList(item + ".lore");
            } else {
                material = Material.valueOf(configManager.getString("settingsMenu." + item + ".item"));
                itemName = configManager.getString("settingsMenu." + item + ".title");
                itemLore = configManager.getStringList("settingsMenu." + item + ".lore");
            }
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
