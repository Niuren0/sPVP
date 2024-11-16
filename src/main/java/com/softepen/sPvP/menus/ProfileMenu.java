package com.softepen.sPvP.menus;

import com.softepen.sPvP.managers.PlayerSettings;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.*;

public class ProfileMenu {
    public ProfileMenu (Player opener, Player player) {
        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("profileMenu.title").replace("{player}", player.getName())))
                .rows(configManager.getInt("profileMenu.row"))
                .create();

        GuiItem profileItem = ItemBuilder.from(getPlayerSkull(player)).asGuiItem();

        GuiItem last5deathItem = ItemBuilder.from(getItemStack("last5deaths", player)).asGuiItem();
        GuiItem last5killItem = ItemBuilder.from(getItemStack("last5kills", player)).asGuiItem();
        GuiItem top5deathItem = ItemBuilder.from(getItemStack("top5deaths", player)).asGuiItem();
        GuiItem top5killItem = ItemBuilder.from(getItemStack("top5kills", player)).asGuiItem();

        gui.setItem(configManager.getInt("profileMenu.profile.slot"), profileItem);
        gui.setItem(configManager.getInt("profileMenu.last5deaths.slot"), last5deathItem);
        gui.setItem(configManager.getInt("profileMenu.last5kills.slot"), last5killItem);
        gui.setItem(configManager.getInt("profileMenu.top5deaths.slot"), top5deathItem);
        gui.setItem(configManager.getInt("profileMenu.top5kills.slot"), top5killItem);

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
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                meta.setLore(coloredLore);
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    private ItemStack getPlayerSkull(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(player);

            headMeta.setDisplayName(player.getDisplayName());
            List<String> itemLore = configManager.getStringList("profileMenu.profile.lore");
            if (itemLore != null) {
                List<String> coloredLore = new ArrayList<>();
                for (String lore : itemLore) {
                    lore = lore
                            .replace("{kills}", kills.getOrDefault(player, 0).toString())
                            .replace("{deaths}", deaths.getOrDefault(player, 0).toString())
                            .replace("{kd}", String.valueOf(getKD(player)))
                            .replace("{lastCombo}", criticalHitLastCombo.getOrDefault(player, 0).toString())
                            .replace("{comboRecord}", getComboRecord(player))
                            .replace("{killSeries}", killSeries.getOrDefault(player, 0).toString())
                            .replace("{killSeriesRecord}", getKillSeriesRecord(player));

                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                headMeta.setLore(coloredLore);
            }

            head.setItemMeta(headMeta);
        }
        else {
            plugin.getLogger().warning("Can not find head meta for " + player.getName());
        }

        return head;
    }

    private String getComboRecord(Player player) {
        PlayerSettings settings = playerSettings.get(player);

        int savedRecord = settings.getComboRecord();
        int hashmapRecord = criticalHitComboRecord.getOrDefault(player, 0);

        return String.valueOf(Math.max(hashmapRecord, savedRecord));
    }

    private String getKillSeriesRecord(Player player) {
        PlayerSettings settings = playerSettings.get(player);

        int savedRecord = settings.getKillSeriesRecord();
        int hashmapRecord = killSeriesRecord.getOrDefault(player, 0);

        return String.valueOf(Math.max(hashmapRecord, savedRecord));
    }
}
