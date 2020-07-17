package org.openredstone.patch;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
    public void onFireworksShoot(EntityShootBowEvent event) {
        if (!event.getProjectile().getType().equals(EntityType.FIREWORK)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (fireworkCount >= maxCount) {
            event.setCancelled(true);
            return;
        }

        fireworkCount++;
        Firework firework = (Firework) event.getProjectile();
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        filterPower(fireworkMeta);
        filterEffects(fireworkMeta);
        firework.setFireworkMeta(fireworkMeta);
        event.setProjectile(firework);
        startDecrementTimer(fireworkMeta.getPower());

    }

    @EventHandler
    public void onFireworksSpawn(EntitySpawnEvent event) {
        if (!event.getEntityType().equals(EntityType.FIREWORK)) {
            return;
        }

        event.setCancelled(false);
        Firework firework = (Firework) event.getEntity();
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        filterEffects(fireworkMeta);
        filterPower(fireworkMeta);
        firework.setFireworkMeta(fireworkMeta);

    }

    @EventHandler
    public void onFireworksUse(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        if (!event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (fireworkCount >= maxCount) {
            event.setCancelled(true);
            return;
        }

        fireworkCount++;
        event.getItem().setItemMeta(filterPower((FireworkMeta) event.getItem().getItemMeta()));
        event.getItem().setItemMeta(filterEffects((FireworkMeta) event.getItem().getItemMeta()));
        startDecrementTimer(((FireworkMeta) event.getItem().getItemMeta()).getPower());

    }

    @EventHandler
    public void onFireworkDispense(BlockDispenseEvent event) {
        if (!event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (fireworkCount >= maxCount) {
            event.setCancelled(true);
            return;
        }

        fireworkCount++;
        event.getItem().setItemMeta(filterPower((FireworkMeta) event.getItem().getItemMeta()));
        event.getItem().setItemMeta(filterEffects((FireworkMeta) event.getItem().getItemMeta()));
        startDecrementTimer(((FireworkMeta) event.getItem().getItemMeta()).getPower());

    }

    private void startDecrementTimer(int power) {
        new BukkitRunnable() {
            @Override
            public void run() {
                fireworkCount--;
            }
        }.runTaskLater(this.plugin, 20*power);
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
