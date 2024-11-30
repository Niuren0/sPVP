package com.softepen.sPvP.events;

import com.softepen.sPvP.managers.PlayerSettings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static com.softepen.sPvP.sPvP.*;
import static com.softepen.sPvP.utils.*;

public class DamageEvent implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Player attacker) {
            if (event.getEntity() instanceof Player victim) {
                if (isPlayerInDisabledRegion(attacker, "hit") || isPlayerInDisabledRegion(victim, "hit")) return;
                if (isPlayerInDisabledWorld(attacker, "hit") || isPlayerInDisabledWorld(victim, "hit")) return;

                if (frozenPlayers.containsKey(attacker)) {
                    event.setCancelled(true);
                    attacker.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", frozenPlayers.get(attacker).getName()));
                    return;
                }
                if (frozenPlayers.containsKey(victim)) {
                    event.setCancelled(true);
                    victim.sendMessage(messagesManager.getPrefixString("frozenBy").replace("{staff}", frozenPlayers.get(victim).getName()));
                    return;
                }

                boolean isCritical = attacker.getFallDistance() > 0.0 &&
                        !((LivingEntity) attacker).isOnGround() &&
                        !attacker.isInWater() &&
                        !attacker.hasPotionEffect(PotionEffectType.BLINDNESS) &&
                        !attacker.hasPotionEffect(PotionEffectType.SLOW_FALLING) &&
                        !attacker.isInsideVehicle() &&
                        attacker.getVelocity().getY() + 0.0784000015258789 <= 0 &&
                        attacker.getAttackCooldown() > 0.9;

                PlayerSettings settings = playerSettings.get(attacker);

                if (isCritical){
                    int currentCombo = criticalHitCombo.getOrDefault(attacker, 0);
                    currentCombo++;

                    if (configManager.getBoolean("ipPrevent.hit")) {
                        if (!Objects.equals(getPlayerIP(attacker), getPlayerIP(victim))) {
                            criticalHitCombo.put(attacker, currentCombo);
                        }
                    }

                    if (settings.getSound()) {
                        Sound criticalSound = getComboSound(currentCombo, attacker.getName(), settings);
                        if (criticalSound != null) {
                            float soundPitch = getComboSoundPitch(currentCombo, settings);
                            attacker.playSound(attacker.getLocation(), criticalSound, 1.0f, soundPitch);
                        }
                    }

                    if (settings.getComboMessages()) {
                        String comboMessage = getComboMessage(currentCombo);
                        if (comboMessage != null) {
                            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(comboMessage));
                        }
                    }

                    if (configManager.getBoolean("ipPrevent.hit")) {
                        if (!Objects.equals(getPlayerIP(attacker), getPlayerIP(victim))) {
                            int finalCurrentCombo = currentCombo;
                            int cooldownDuration = configManager.getInt("comboCooldown");
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                int lastCombo = criticalHitCombo.getOrDefault(attacker, 0);

                                if (finalCurrentCombo == lastCombo) {
                                    setCombo(attacker);

                                    criticalHitCombo.put(attacker, 0);
                                }
                            }, (long) cooldownDuration * 20);
                        }
                    }

                } else {
                    if (configManager.getBoolean("ipPrevent.hit")) {
                        if (!Objects.equals(getPlayerIP(attacker), getPlayerIP(victim))) {
                            if (criticalHitCombo.getOrDefault(attacker, 0) != 0) {
                                setCombo(attacker);
                                criticalHitCombo.put(attacker, 0);
                            }
                        }

                        if (criticalHitCombo.getOrDefault(attacker, 0) != 0) {
                            Sound breakSound = getBreakSound(Sound.ENTITY_VILLAGER_NO);
                            attacker.playSound(attacker.getLocation(), breakSound, 1, 1);
                        }
                    }

                }

                if (settings.getHealthIndicator()) {
                    double healthAfterDamage = victim.getHealth() - event.getDamage();
                    if (healthAfterDamage < 0) healthAfterDamage = 0;

                    String formattedHearts = String.format("%.1f", healthAfterDamage);
                    attacker.sendTitle(" ", getColor(settings.getHealthIndicatorColor()) + "❤" + formattedHearts, 10, 40, 10);
                }

                if (configManager.getBoolean("particlesEnabled")) {
                    String particleName = settings.getParticle();
                    if (particleName != null) {
                        if (attacker.hasPermission("spvp.particles." + particleName)) {
                            try {
                                Particle particle = Particle.valueOf(particleName.toUpperCase());

                                attacker.getWorld().spawnParticle(particle, victim.getLocation(), 10);

                            } catch (IllegalArgumentException e) {
                                plugin.getLogger().warning("Invalid particle name (" + attacker.getName() + "): " + particleName);
                            }
                        }
                    }
                }
            }
        }
    }

    private ChatColor getColor(String input) {
        input = input.replace("_WOOL", "");
        input = input.replace("_CONCRETE", "");
        input = input.replace("_POWDER", "");
        input = input.replace("_STAINED_GLASS", "");
        input = input.replace("_PANE", "");
        input = input.replace("_DYE", "");

        return switch (input) {
            case "ORANGE", "BROWN" -> ChatColor.GOLD;
            case "MAGENTA", "PINK" -> ChatColor.LIGHT_PURPLE;
            case "LIGHT_BLUE" -> ChatColor.AQUA;
            case "YELLOW" -> ChatColor.YELLOW;
            case "LIME" -> ChatColor.GREEN;
            case "GRAY" -> ChatColor.DARK_GRAY;
            case "LIGHT_GRAY" -> ChatColor.GRAY;
            case "CYAN" -> ChatColor.DARK_AQUA;
            case "PURPLE" -> ChatColor.DARK_PURPLE;
            case "BLUE" -> ChatColor.BLUE;
            case "GREEN" -> ChatColor.DARK_GREEN;
            case "RED" -> ChatColor.RED;
            case "BLACK" -> ChatColor.BLACK;
            default -> ChatColor.WHITE;
        };
    }

    private void setCombo(Player player) {
        int combo = criticalHitCombo.getOrDefault(player, 0);
        if (combo > 0) {
            criticalHitLastCombo.put(player, combo);
            if (criticalHitComboRecord.getOrDefault(player, 0) < combo) {
                criticalHitComboRecord.put(player, combo);
            }
        }
    }
}
