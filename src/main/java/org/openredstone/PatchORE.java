package org.openredstone;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.openredstone.patch.DeathPotionsPatch;
import org.openredstone.patch.EnchantmentPatch;
import org.openredstone.patch.ExtendedPistonsPatch;
import org.openredstone.patch.FireworksPatch;
import org.openredstone.patch.SpawnEggsPatch;
import org.openredstone.protoLib.PlayerPositionPacketHandler;

import java.io.File;
import java.util.logging.Level;

public class PatchORE extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        setupConfig();
        loadPatches();
    }

    private void loadPatches() {
        if (config.getBoolean("patches.fireworks")) {
            getServer().getPluginManager().registerEvents(new FireworksPatch(this), this);
        }
        if (config.getBoolean("patches.enchantments")) {
            getServer().getPluginManager().registerEvents(new EnchantmentPatch(this), this);
        }
        if (config.getBoolean("patches.extendedpistons")) {
            getServer().getPluginManager().registerEvents(new ExtendedPistonsPatch(this), this);
        }
        if (config.getBoolean("patches.spawneggs")) {
            getServer().getPluginManager().registerEvents(new SpawnEggsPatch(this), this);
        }
        if (config.getBoolean("patches.deathpotions")) {
            getServer().getPluginManager().registerEvents(new DeathPotionsPatch(this), this);
        }
        if (config.getBoolean("patches.void")) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new PlayerPositionPacketHandler(this));
        }
    }

    private void setupConfig() {
        config = getConfig();
        try {
            if (!getDataFolder().exists()) {
                if (!getDataFolder().mkdir()) {
                    getLogger().log(Level.SEVERE, "Unable to make plugin directory.");
                    this.setEnabled(false);
                }
            }
            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating.");
            } else {
                getLogger().info("Config.yml found, loading.");
            }

            // Patch options
            config.addDefault("patches.fireworks", true);
            config.addDefault("patches.enchantments", true);
            config.addDefault("patches.extendedpistons", true);
            config.addDefault("patches.spawneggs", true);
            config.addDefault("patches.deathpotions", true);
            config.addDefault("patches.void", true);

            // Fireworks options
            config.addDefault("fireworks.power", 5);
            config.addDefault("fireworks.count", 50);
            config.addDefault("fireworks.effects_count", 5);

            // Enchantment options
            config.addDefault("enchantments.arrow_damage.minimum_enchantment",0);
            config.addDefault("enchantments.arrow_damage.maximum_enchantment",100);
            config.addDefault("enchantments.arrow_fire.minimum_enchantment",0);
            config.addDefault("enchantments.arrow_fire.maximum_enchantment",100);
            config.addDefault("enchantments.arrow_infinite.minimum_enchantment",0);
            config.addDefault("enchantments.arrow_infinite.maximum_enchantment",100);
            config.addDefault("enchantments.arrow_knockback.minimum_enchantment",0);
            config.addDefault("enchantments.arrow_knockback.maximum_enchantment",100);
            config.addDefault("enchantments.binding_curse.minimum_enchantment",0);
            config.addDefault("enchantments.binding_curse.maximum_enchantment",100);
            config.addDefault("enchantments.channeling.minimum_enchantment",0);
            config.addDefault("enchantments.channeling.maximum_enchantment",100);
            config.addDefault("enchantments.damage_all.minimum_enchantment",0);
            config.addDefault("enchantments.damage_all.maximum_enchantment",100);
            config.addDefault("enchantments.damage_arthropods.minimum_enchantment",0);
            config.addDefault("enchantments.damage_arthropods.maximum_enchantment",100);
            config.addDefault("enchantments.damage_undead.minimum_enchantment",0);
            config.addDefault("enchantments.damage_undead.maximum_enchantment",100);
            config.addDefault("enchantments.depth_strider.minimum_enchantment",0);
            config.addDefault("enchantments.depth_strider.maximum_enchantment",100);
            config.addDefault("enchantments.dig_speed.minimum_enchantment",0);
            config.addDefault("enchantments.dig_speed.maximum_enchantment",100);
            config.addDefault("enchantments.durability.minimum_enchantment",0);
            config.addDefault("enchantments.durability.maximum_enchantment",100);
            config.addDefault("enchantments.fire_aspect.minimum_enchantment",0);
            config.addDefault("enchantments.fire_aspect.maximum_enchantment",100);
            config.addDefault("enchantments.frost_walker.minimum_enchantment",0);
            config.addDefault("enchantments.frost_walker.maximum_enchantment",100);
            config.addDefault("enchantments.impaling.minimum_enchantment",0);
            config.addDefault("enchantments.impaling.maximum_enchantment",100);
            config.addDefault("enchantments.knockback.minimum_enchantment",0);
            config.addDefault("enchantments.knockback.maximum_enchantment",100);
            config.addDefault("enchantments.loot_bonus_blocks.minimum_enchantment",0);
            config.addDefault("enchantments.loot_bonus_blocks.maximum_enchantment",100);
            config.addDefault("enchantments.loot_bonus_mobs.minimum_enchantment",0);
            config.addDefault("enchantments.loot_bonus_mobs.maximum_enchantment",100);
            config.addDefault("enchantments.loyalty.minimum_enchantment",0);
            config.addDefault("enchantments.loyalty.maximum_enchantment",100);
            config.addDefault("enchantments.luck.minimum_enchantment",0);
            config.addDefault("enchantments.luck.maximum_enchantment",100);
            config.addDefault("enchantments.lure.minimum_enchantment",0);
            config.addDefault("enchantments.lure.maximum_enchantment",100);
            config.addDefault("enchantments.mending.minimum_enchantment",0);
            config.addDefault("enchantments.mending.maximum_enchantment",100);
            config.addDefault("enchantments.multishot.minimum_enchantment",0);
            config.addDefault("enchantments.multishot.maximum_enchantment",100);
            config.addDefault("enchantments.oxygen.minimum_enchantment",0);
            config.addDefault("enchantments.oxygen.maximum_enchantment",100);
            config.addDefault("enchantments.piercing.minimum_enchantment",0);
            config.addDefault("enchantments.piercing.maximum_enchantment",100);
            config.addDefault("enchantments.protection_environmental.minimum_enchantment",0);
            config.addDefault("enchantments.protection_environmental.maximum_enchantment",100);
            config.addDefault("enchantments.protection_explosions.minimum_enchantment",0);
            config.addDefault("enchantments.protection_explosions.maximum_enchantment",100);
            config.addDefault("enchantments.protection_fall.minimum_enchantment",0);
            config.addDefault("enchantments.protection_fall.maximum_enchantment",100);
            config.addDefault("enchantments.protection_fire.minimum_enchantment",0);
            config.addDefault("enchantments.protection_fire.maximum_enchantment",100);
            config.addDefault("enchantments.protection_projectile.minimum_enchantment",0);
            config.addDefault("enchantments.protection_projectile.maximum_enchantment",100);
            config.addDefault("enchantments.quick_charge.minimum_enchantment",0);
            config.addDefault("enchantments.quick_charge.maximum_enchantment",100);
            config.addDefault("enchantments.riptide.minimum_enchantment",0);
            config.addDefault("enchantments.riptide.maximum_enchantment",10);
            config.addDefault("enchantments.silk_touch.minimum_enchantment",0);
            config.addDefault("enchantments.silk_touch.maximum_enchantment",10);
            config.addDefault("enchantments.sweeping_edge.minimum_enchantment",0);
            config.addDefault("enchantments.sweeping_edge.maximum_enchantment",10);
            config.addDefault("enchantments.thorns.minimum_enchantment",0);
            config.addDefault("enchantments.thorns.maximum_enchantment",10);
            config.addDefault("enchantments.vanishing_curse.minimum_enchantment",0);
            config.addDefault("enchantments.vanishing_curse.maximum_enchantment",100);
            config.addDefault("enchantments.water_worker.minimum_enchantment",0);
            config.addDefault("enchantments.water_worker.maximum_enchantment",100);

            // SpawnEggs options
            config.addDefault("spawneggs.entity.potion.block", true);
            config.addDefault("spawneggs.entity.fireball.block", false);
            config.addDefault("spawneggs.entity.fireball.explosion_power", 500);
            config.addDefault("spawneggs.entity.fireball.explosion_radius", 10);

            // DeathPotion options
            config.addDefault("deathpotions.max_amplifier", 32);
            config.addDefault("deathpotions.max_duration", 1000);

            config.options().copyDefaults(true);
            saveConfig();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
