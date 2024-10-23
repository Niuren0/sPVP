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
import java.util.List;
import java.util.ListIterator;
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

            if (player.hasPermission("spvp.messages." + key)) {
                PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);
                if (Objects.equals(settings.getKillMessage(), key)) {
                    guiItem = ItemBuilder.from(getItemStack("selected", message)).asGuiItem(event -> {
                        event.setCancelled(true);

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
                        event.setCancelled(true);

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
                guiItem = ItemBuilder.from(getItemStack("hasNotPerm", message)).asGuiItem(event -> event.setCancelled(true));
            }

            gui.setItem(i, guiItem);
            i++;
        }

        GuiItem backItem = ItemBuilder.from(getItemStack("back", "")).asGuiItem(event -> {
            event.setCancelled(true);
            new SettingsMenu(player);
        });
        gui.setItem(configManager.getInt("killMessagesMenu.back.slot"), backItem);

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(player);
    }

    private ItemStack getItemStack(String s, String message) {
        Material material = Material.valueOf(configManager.getString("killMessagesMenu." + s + ".item"));
        String itemName = configManager.getString("killMessagesMenu." + s + ".title").replace("{message}", message);
        List<String> itemLore = configManager.getStringList("killMessagesMenu." + s + ".lore");

        if (itemLore != null) {
            ListIterator<String> iterator = itemLore.listIterator();
            while (iterator.hasNext()) {
                String lore = iterator.next();
                lore = lore.replace("{message}", message);
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
