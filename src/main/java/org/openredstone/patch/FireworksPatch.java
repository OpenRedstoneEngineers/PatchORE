package org.openredstone.patch;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.openredstone.PatchORE;

public class FireworksPatch extends Patch implements Listener {

    private int fireworkCount = 0;
    private int maxCount = PatchORE.config.getInt("fireworks.count");
    private int maxPower = PatchORE.config.getInt("fireworks.power");
    private int maxEffectsCount = PatchORE.config.getInt("fireworks.effects_count");

    public FireworksPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        fireworkCount--;
        Firework firework = event.getEntity();
        firework.setFireworkMeta(filterPower(firework.getFireworkMeta()));
        firework.setFireworkMeta(filterEffects(firework.getFireworkMeta()));
    }

    @EventHandler
    public void onFireworksUse(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        if (!event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }
        if (fireworkCount < maxCount) {
            fireworkCount++;
            event.getItem().setItemMeta(filterPower((FireworkMeta) event.getItem().getItemMeta()));
            event.getItem().setItemMeta(filterEffects((FireworkMeta) event.getItem().getItemMeta()));
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireworkDispense(BlockDispenseEvent event) {
        if (!event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }
        if (fireworkCount < maxCount) {
            fireworkCount++;
            event.getItem().setItemMeta(filterPower((FireworkMeta) event.getItem().getItemMeta()));
            event.getItem().setItemMeta(filterEffects((FireworkMeta) event.getItem().getItemMeta()));
        } else {
            event.setCancelled(true);
        }
    }

    private FireworkMeta filterPower(FireworkMeta meta) {
        if (meta.getPower() > maxPower) {
            meta.setPower(maxPower);
        }
        return meta;
    }

    private FireworkMeta filterEffects(FireworkMeta meta) {
        if (meta.hasEffects() && meta.getEffectsSize() > maxEffectsCount) {
            Iterable<FireworkEffect> sublist = meta.getEffects().subList(0, maxEffectsCount);
            meta.clearEffects();
            meta.addEffects(sublist);
        }
        return meta;
    }
}
