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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DeathPotionsPatch extends Patch implements Listener {

    private int maxAmplifier = PatchORE.config.getInt("deathpotions.max_amplifier");
    private int maxDuration = PatchORE.config.getInt("deathpotions.max_duration");

    private final HashSet<PotionEffectType> safeEffectsSet = new HashSet<>(Arrays.asList(
        //PotionEffectType.POISON,
        //PotionEffectType.WITHER,
        //PotionEffectType.HEALTH_BOOST,
        //PotionEffectType.ABSORPTION,
        //PotionEffectType.HEAL,
        //PotionEffectType.HARM,
        //PotionEffectType.REGENERATION,

        //PotionEffectType.INCREASE_DAMAGE,
        //PotionEffectType.SPEED,
        //PotionEffectType.DOLPHINS_GRACE,
        //PotionEffectType.CONFUSION,
        PotionEffectType.SLOW,
        PotionEffectType.FAST_DIGGING,
        PotionEffectType.SLOW_DIGGING,
        PotionEffectType.JUMP,
        PotionEffectType.DAMAGE_RESISTANCE,
        PotionEffectType.FIRE_RESISTANCE,
        PotionEffectType.WATER_BREATHING,
        PotionEffectType.INVISIBILITY,
        PotionEffectType.BLINDNESS,
        PotionEffectType.NIGHT_VISION,
        PotionEffectType.HUNGER,
        PotionEffectType.WEAKNESS,
        PotionEffectType.SATURATION,
        PotionEffectType.GLOWING,
        PotionEffectType.LEVITATION,
        PotionEffectType.LUCK,
        PotionEffectType.UNLUCK,
        PotionEffectType.SLOW_FALLING,
        PotionEffectType.CONDUIT_POWER,
        PotionEffectType.BAD_OMEN,
        PotionEffectType.HERO_OF_THE_VILLAGE
    ));


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
            if(!safeEffectsSet.contains(effect.getType())) {
                if (effect.getType().equals(PotionEffectType.HEAL) && (effect.getAmplifier() > maxAmplifier)) {
                    return false;
                }
                if (effect.getDuration() > maxDuration) {
                    return false;
                }
            }
        }
        return true;
    }
}
