package org.openredstone;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.openredstone.patch.*;
import org.openredstone.protoLib.PlayerPositionPacketHandler;
import org.openredstone.protoLib.PlayerInventoryPacketHandler;

import java.io.File;
import java.util.logging.Level;

public class PatchORE extends JavaPlugin {

    public static FileConfiguration config;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
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
            protocolManager.addPacketListener(new PlayerPositionPacketHandler(this));
        }
        if (config.getBoolean("patches.nbt")) {
            protocolManager.addPacketListener(new PlayerInventoryPacketHandler(this));
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
            config.addDefault("patches.antiup", false);
            config.addDefault("patches.fireworks", true);
            config.addDefault("patches.enchantments", true);
            config.addDefault("patches.extendedpistons", true);
            config.addDefault("patches.spawneggs", true);
            config.addDefault("patches.deathpotions", true);
            config.addDefault("patches.void", true);
            config.addDefault("patches.nbt", true);

            // Fireworks options
            config.addDefault("fireworks.power", 5);
            config.addDefault("fireworks.count", 50);
            config.addDefault("fireworks.effects_count", 5);

            // Enchantment options
            config.addDefault("enchantments.power.minimum_enchantment",0);
            config.addDefault("enchantments.power.maximum_enchantment",100);
            config.addDefault("enchantments.flame.minimum_enchantment",0);
            config.addDefault("enchantments.flame.maximum_enchantment",100);
            config.addDefault("enchantments.infinity.minimum_enchantment",0);
            config.addDefault("enchantments.infinity.maximum_enchantment",100);
            config.addDefault("enchantments.punch.minimum_enchantment",0);
            config.addDefault("enchantments.punch.maximum_enchantment",100);
            config.addDefault("enchantments.binding_curse.minimum_enchantment",0);
            config.addDefault("enchantments.binding_curse.maximum_enchantment",100);
            config.addDefault("enchantments.channeling.minimum_enchantment",0);
            config.addDefault("enchantments.channeling.maximum_enchantment",100);
            config.addDefault("enchantments.sharpness.minimum_enchantment",0);
            config.addDefault("enchantments.sharpness.maximum_enchantment",100);
            config.addDefault("enchantments.bane_of_arthropods.minimum_enchantment",0);
            config.addDefault("enchantments.bane_of_arthropods.maximum_enchantment",100);
            config.addDefault("enchantments.smite.minimum_enchantment",0);
            config.addDefault("enchantments.smite.maximum_enchantment",100);
            config.addDefault("enchantments.depth_strider.minimum_enchantment",0);
            config.addDefault("enchantments.depth_strider.maximum_enchantment",100);
            config.addDefault("enchantments.efficiency.minimum_enchantment",0);
            config.addDefault("enchantments.efficiency.maximum_enchantment",100);
            config.addDefault("enchantments.unbreaking.minimum_enchantment",0);
            config.addDefault("enchantments.unbreaking.maximum_enchantment",100);
            config.addDefault("enchantments.fire_aspect.minimum_enchantment",0);
            config.addDefault("enchantments.fire_aspect.maximum_enchantment",100);
            config.addDefault("enchantments.frost_walker.minimum_enchantment",0);
            config.addDefault("enchantments.frost_walker.maximum_enchantment",100);
            config.addDefault("enchantments.impaling.minimum_enchantment",0);
            config.addDefault("enchantments.impaling.maximum_enchantment",100);
            config.addDefault("enchantments.knockback.minimum_enchantment",0);
            config.addDefault("enchantments.knockback.maximum_enchantment",100);
            config.addDefault("enchantments.fortune.minimum_enchantment",0);
            config.addDefault("enchantments.fortune.maximum_enchantment",100);
            config.addDefault("enchantments.looting.minimum_enchantment",0);
            config.addDefault("enchantments.looting.maximum_enchantment",100);
            config.addDefault("enchantments.loyalty.minimum_enchantment",0);
            config.addDefault("enchantments.loyalty.maximum_enchantment",100);
            config.addDefault("enchantments.luck_of_the_sea.minimum_enchantment",0);
            config.addDefault("enchantments.luck_of_the_sea.maximum_enchantment",100);
            config.addDefault("enchantments.lure.minimum_enchantment",0);
            config.addDefault("enchantments.lure.maximum_enchantment",100);
            config.addDefault("enchantments.mending.minimum_enchantment",0);
            config.addDefault("enchantments.mending.maximum_enchantment",100);
            config.addDefault("enchantments.multishot.minimum_enchantment",0);
            config.addDefault("enchantments.multishot.maximum_enchantment",100);
            config.addDefault("enchantments.respiration.minimum_enchantment",0);
            config.addDefault("enchantments.respiration.maximum_enchantment",100);
            config.addDefault("enchantments.piercing.minimum_enchantment",0);
            config.addDefault("enchantments.piercing.maximum_enchantment",100);
            config.addDefault("enchantments.protection.minimum_enchantment",0);
            config.addDefault("enchantments.protection.maximum_enchantment",100);
            config.addDefault("enchantments.blast_protection.minimum_enchantment",0);
            config.addDefault("enchantments.blast_protection.maximum_enchantment",100);
            config.addDefault("enchantments.feather_falling.minimum_enchantment",0);
            config.addDefault("enchantments.feather_falling.maximum_enchantment",100);
            config.addDefault("enchantments.fire_protection.minimum_enchantment",0);
            config.addDefault("enchantments.fire_protection.maximum_enchantment",100);
            config.addDefault("enchantments.projectile_protection.minimum_enchantment",0);
            config.addDefault("enchantments.projectile_protection.maximum_enchantment",100);
            config.addDefault("enchantments.quick_charge.minimum_enchantment",0);
            config.addDefault("enchantments.quick_charge.maximum_enchantment",100);
            config.addDefault("enchantments.riptide.minimum_enchantment",0);
            config.addDefault("enchantments.riptide.maximum_enchantment",10);
            config.addDefault("enchantments.silk_touch.minimum_enchantment",0);
            config.addDefault("enchantments.silk_touch.maximum_enchantment",10);
            config.addDefault("enchantments.sweeping.minimum_enchantment",0);
            config.addDefault("enchantments.sweeping.maximum_enchantment",10);
            config.addDefault("enchantments.thorns.minimum_enchantment",0);
            config.addDefault("enchantments.thorns.maximum_enchantment",10);
            config.addDefault("enchantments.vanishing_curse.minimum_enchantment",0);
            config.addDefault("enchantments.vanishing_curse.maximum_enchantment",100);
            config.addDefault("enchantments.aqua_affinity.minimum_enchantment",0);
            config.addDefault("enchantments.aqua_affinity.maximum_enchantment",100);

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
