package com.softepen.sPvP.menus;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.applyLast5;
import static com.softepen.sPvP.utils.applyTop5;

public class ProfileMenu {
    public ProfileMenu (Player opener, Player player) {
        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("profileMenu.title").replace("{player}", player.getName())))
                .rows(configManager.getInt("profileMenu.row"))
                .create();

        GuiItem deathItem = ItemBuilder.from(getItemStack("deaths", player)).asGuiItem();
        GuiItem killItem = ItemBuilder.from(getItemStack("kills", player)).asGuiItem();
        GuiItem last5deathItem = ItemBuilder.from(getItemStack("last5deaths", player)).asGuiItem();
        GuiItem last5killItem = ItemBuilder.from(getItemStack("last5kills", player)).asGuiItem();
        GuiItem top5deathItem = ItemBuilder.from(getItemStack("top5deaths", player)).asGuiItem();
        GuiItem top5killItem = ItemBuilder.from(getItemStack("top5kills", player)).asGuiItem();
        GuiItem lastComboItem = ItemBuilder.from(getItemStack("lastCombo", player)).asGuiItem();
        GuiItem comboRecordItem = ItemBuilder.from(getItemStack("comboRecord", player)).asGuiItem();
        GuiItem currentStreakItem = ItemBuilder.from(getItemStack("currentStreak", player)).asGuiItem();
        GuiItem streakRecordItem = ItemBuilder.from(getItemStack("streakRecord", player)).asGuiItem();

        gui.setItem(configManager.getInt("profileMenu.deaths.slot"), deathItem);
        gui.setItem(configManager.getInt("profileMenu.kills.slot"), killItem);
        gui.setItem(configManager.getInt("profileMenu.last5deaths.slot"), last5deathItem);
        gui.setItem(configManager.getInt("profileMenu.last5kills.slot"), last5killItem);
        gui.setItem(configManager.getInt("profileMenu.top5deaths.slot"), top5deathItem);
        gui.setItem(configManager.getInt("profileMenu.top5kills.slot"), top5killItem);
        gui.setItem(configManager.getInt("profileMenu.lastCombo.slot"), lastComboItem);
        gui.setItem(configManager.getInt("profileMenu.comboRecord.slot"), comboRecordItem);
        gui.setItem(configManager.getInt("profileMenu.currentStreak.slot"), currentStreakItem);
        gui.setItem(configManager.getInt("profileMenu.streakRecord.slot"), streakRecordItem);

        GuiItem backItem = ItemBuilder.from(getItemStack("back", player)).asGuiItem(event -> new SettingsMenu(opener));
        gui.setItem(configManager.getInt("profileMenu.back.slot"), backItem);

        if (configManager.getBoolean("profileMenu.filler.enable")) {
            for (String key : configManager.getConfigurationSection("profileMenu.filler.items").getKeys(false)) {
                List<Integer> slots = configManager.getIntegerList("profileMenu.filler.items." + key + ".slots");
                GuiItem fillerItem = ItemBuilder.from(getItemStack("profileMenu.filler.items." + key, player)).asGuiItem();
                for (Integer slot : slots) {
                    gui.setItem(slot, fillerItem);
                }
            }
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.open(opener);
    }

    private ItemStack getItemStack(String s, Player player) {
        Material material;
        String itemName;
        List<String> itemLore;
        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

        if (s.startsWith("profileMenu.filler.items.")) {
            material = Material.valueOf(configManager.getString(s + ".item"));
            itemName = configManager.getString(s + ".title");
            itemLore = configManager.getStringList(s + ".lore");
        } else {
            material = Material.valueOf(configManager.getString("profileMenu." + s + ".item"));
            itemName = configManager.getString("profileMenu." + s + ".title");
            itemLore = configManager.getStringList("profileMenu." + s + ".lore");
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (itemName != null) {
                itemName = itemName.replace("{player}", player.getName());
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            }
            if (itemLore != null) {
                if (Objects.equals(s, "top5deaths") || Objects.equals(s, "top5kills")) {
                    itemLore = applyTop5(s, itemLore, player.getName());
                } else if (Objects.equals(s, "last5deaths") || Objects.equals(s, "last5kills")) {
                    itemLore = applyLast5(s, itemLore, player.getName());
                }
                List<String> coloredLore = new ArrayList<>();
                for (String lore : itemLore) {
                    lore = lore.replace("{player}", player.getName());

                    String death = deaths.getOrDefault(player, settings.getDeaths()).toString();
                    String kill = kills.getOrDefault(player, settings.getKills()).toString();
                    String lastCombo = criticalHitLastCombo.getOrDefault(player, settings.getLastCombo()).toString();
                    String comboRecord = criticalHitComboRecord.getOrDefault(player, settings.getComboRecord()).toString();
                    String currentStreak = killSeries.getOrDefault(player, 0).toString();
                    String streakRecord = killSeriesRecord.getOrDefault(player, settings.getKillSeriesRecord()).toString();

                    if (Objects.equals(s, "deaths")) lore = lore.replace("{" + s + "}", death);
                    if (Objects.equals(s, "kills")) lore = lore.replace("{" + s + "}", kill);
                    if (Objects.equals(s, "lastCombo")) lore = lore.replace("{" + s + "}", lastCombo);
                    if (Objects.equals(s, "comboRecord")) lore = lore.replace("{" + s + "}", comboRecord);
                    if (Objects.equals(s, "currentStreak")) lore = lore.replace("{" + s + "}", currentStreak);
                    if (Objects.equals(s, "streakRecord")) lore = lore.replace("{" + s + "}", streakRecord);
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', lore));

                }
                meta.setLore(coloredLore);
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
