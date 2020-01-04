package org.openredstone.patch;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.openredstone.PatchORE;

import java.util.ArrayList;
import java.util.List;

public class DeathPotionsPatch extends Patch implements Listener {

    private int maxAmplifier = PatchORE.config.getInt("deathpotions.max_amplifier");
    private int maxDuration = PatchORE.config.getInt("deathpotions.max_duration");

    public DeathPotionsPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void consumePotionEvent(PlayerItemConsumeEvent event) {
        if (!event.getItem().getType().equals(Material.POTION)) {
            return;
        }

        PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();

        if (hasLegalEffects(meta.getCustomEffects())) {
            return;
        }

        event.setCancelled(true);

        sendMessage(event.getPlayer(), "We just saved you from possible death. You're welcome.");

    }

    @EventHandler
    public void splashPotionEvent(PotionSplashEvent event) {
        if (hasLegalEffects(new ArrayList<>(event.getPotion().getEffects()))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void lingeringSplashPotionEvent(LingeringPotionSplashEvent event) {
        if (hasLegalEffects(event.getAreaEffectCloud().getCustomEffects())) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean hasLegalEffects(List<PotionEffect> potionEffects) {
        for (PotionEffect effect : potionEffects) {
            if (effect.getType().equals(PotionEffectType.HEAL) && (effect.getAmplifier() > maxAmplifier)) {
                return false;
            }
            if (effect.getDuration() > maxDuration) {
                return false;
            }
        }
        return true;
    }
}
