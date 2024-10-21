package com.softepen.sPvP.menus;

import com.softepen.sPvP.managers.PlayerSettings;
import com.softepen.sPvP.managers.PlayerSettingsManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.softepen.sPvP.sPvP.plugin;
import static com.softepen.sPvP.sPvP.configManager;

public class SettingsMenu {
    public SettingsMenu (Player player) {
        String playerName = player.getName();

        Gui gui = Gui.gui()
                .title(Component.text(configManager.getString("settingsMenu.title")))
                .rows(configManager.getInt("settingsMenu.row"))
                .create();

        GuiItem enableItem = ItemBuilder.from(getItemStack(player, "enable")).asGuiItem(event -> {
            event.setCancelled(true);

            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

            playerData.set("healthIndicator", !settings.getHealthIndicator());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            gui.updateItem(configManager.getInt("settingsMenu.enable.slot"), new ItemStack(getItemStack(player, "enable")));
        });

        GuiItem colorItem = ItemBuilder.from(getItemStack(player, "color")).asGuiItem(event -> {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            assert item != null;
            String itemName = item.getType().name();

            Material[] colors = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
                    Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL,
                    Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL,
                    Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
                    Material.BLACK_WOOL};

            if (itemName.contains("CONCRETE")) {
                colors = new Material[]{Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE,
                        Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.PINK_CONCRETE,
                        Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE,
                        Material.BLUE_CONCRETE, Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE,
                        Material.BLACK_CONCRETE};
            } else if (itemName.contains("CONCRETE_POWDER")) {
                colors = new Material[]{Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER,
                        Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER,
                        Material.GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER,
                        Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER,
                        Material.BLACK_CONCRETE_POWDER};
            } else if (itemName.contains("STAINED_GLASS")) {
                colors = new Material[]{Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS,
                        Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS,
                        Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS,
                        Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS,
                        Material.BLACK_STAINED_GLASS};
            } else if (itemName.contains("STAINED_GLASS_PANE")) {
                colors = new Material[]{Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
                        Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE,
                        Material.GRAY_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE,
                        Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE,
                        Material.BLACK_STAINED_GLASS_PANE};
            } else if (itemName.contains("DYE")) {
                colors = new Material[]{Material.WHITE_DYE, Material.ORANGE_DYE, Material.MAGENTA_DYE,
                        Material.LIGHT_BLUE_DYE, Material.YELLOW_DYE, Material.LIME_DYE, Material.PINK_DYE,
                        Material.GRAY_DYE, Material.LIGHT_GRAY_DYE, Material.CYAN_DYE, Material.PURPLE_DYE,
                        Material.BLUE_DYE, Material.BROWN_DYE, Material.GREEN_DYE, Material.RED_DYE,
                        Material.BLACK_DYE};
            }

            int currentIndex = -1;
            for (int i = 0; i < colors.length; i++) {
                if (item.getType() == colors[i]) {
                    currentIndex = i;
                    break;
                }
            }

            int newIndex;
            if (event.getClick().isLeftClick()) newIndex = (currentIndex + 1) % colors.length;
            else if (event.getClick().isRightClick()) newIndex = (currentIndex - 1 + colors.length) % colors.length;
            else return;

            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            playerData.set("healthIndicatorColor", colors[newIndex].name());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            gui.updateItem(configManager.getInt("settingsMenu.color.slot"), new ItemStack(getItemStack(player, "color")));
        });
        
        GuiItem soundItem = ItemBuilder.from(getItemStack(player, "sound")).asGuiItem(event -> {
           event.setCancelled(true);

            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

            playerData.set("sound", !settings.getSound());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            gui.updateItem(configManager.getInt("settingsMenu.sound.slot"), new ItemStack(getItemStack(player, "sound")));
        });

        GuiItem killMessagesItem = ItemBuilder.from(getItemStack(player, "killMessages")).asGuiItem(event -> {
            event.setCancelled(true);
            new KillMessagesSelector(player);
        });

        GuiItem comboMessagesItem = ItemBuilder.from(getItemStack(player, "comboMessages")).asGuiItem(event -> {
            event.setCancelled(true);

            File playerFile = new File(plugin.getDataFolder(), "data/" + playerName + ".yml");
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

            playerData.set("comboMessages", !settings.getComboMessages());

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("An error occurred when saving player data file: " + e);
            }

            gui.updateItem(configManager.getInt("settingsMenu.comboMessages.slot"), new ItemStack(getItemStack(player, "comboMessages")));
        });

        gui.setItem(configManager.getInt("settingsMenu.sound.slot"), soundItem);
        gui.setItem(configManager.getInt("settingsMenu.killMessages.slot"), killMessagesItem);
        gui.setItem(configManager.getInt("settingsMenu.comboMessages.slot"), comboMessagesItem);
        gui.setItem(configManager.getInt("settingsMenu.enable.slot"), enableItem);
        gui.setItem(configManager.getInt("settingsMenu.color.slot"), colorItem);
        if (configManager.getBoolean("settingsMenu.filler.enable")) {
            gui.getFiller().fill(ItemBuilder.from(getItemStack(player, "filler")).asGuiItem());
        }

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.open(player);
    }

    private ItemStack getItemStack(Player player, String item){
        PlayerSettings settings = PlayerSettingsManager.getPlayerSettings(player);

        Material material = Material.AIR;
        ItemStack itemStack;
        String state, itemName = null;
        List<String> itemLore = null;

        if (Objects.equals(item, "sound")) {
            state = settings.getSound() ? "enabled_state" : "disabled_state";
            material = Material.valueOf(configManager.getString("settingsMenu.sound." + state + ".item"));

            itemName = configManager.getString("settingsMenu.sound." + state + ".title");
            itemLore = configManager.getStringList("settingsMenu.sound." + state + ".lore");
        } else if (Objects.equals(item, "killMessages")) {
            material = Material.valueOf(configManager.getString("settingsMenu.killMessages.item"));

            itemName = configManager.getString("settingsMenu.killMessages.title");
            itemLore = configManager.getStringList("settingsMenu.killMessages.lore");
        } else if (Objects.equals(item, "comboMessages")) {
            state = settings.getComboMessages() ? "enabled_state" : "disabled_state";
            material = Material.valueOf(configManager.getString("settingsMenu.comboMessages." + state + ".item"));
            itemName = configManager.getString("settingsMenu.comboMessages." + state + ".title");
            itemLore = configManager.getStringList("settingsMenu.comboMessages." + state + ".lore");
        } else if (Objects.equals(item, "enable")) {
            state = settings.getHealthIndicator() ? "enabled_state" : "disabled_state";
            material = Material.valueOf(configManager.getString("settingsMenu.enable." + state + ".item"));
            itemName = configManager.getString("settingsMenu.enable." + state + ".title");
            itemLore = configManager.getStringList("settingsMenu.enable." + state + ".lore");
        } else if (Objects.equals(item, "color")) {
            String color = getRawColor(settings.getHealthIndicatorColor());
            material = Material.valueOf(color + configManager.getString("settingsMenu.color.item"));
            itemName = configManager.getString("settingsMenu.color.title");
            itemLore = configManager.getStringList("settingsMenu.color.lore");
        } else if (Objects.equals(item, "filler")) {
            material = Material.valueOf(configManager.getString("settingsMenu.filler.item"));
            itemName = configManager.getString("settingsMenu.filler.title");
            itemLore = configManager.getStringList("settingsMenu.filler.lore");
        }

        itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (itemName != null) meta.setDisplayName(itemName);
            if (itemLore != null) meta.setLore(itemLore);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static String getRawColor(String input) {
        input = input.replace("WOOL", "");
        input = input.replace("CONCRETE", "");
        input = input.replace("CONCRETE_POWDER", "");
        input = input.replace("STAINED_GLASS", "");
        input = input.replace("STAINED_GLASS_PANE", "");
        input = input.replace("DYE", "");

        return input;
    }
}
