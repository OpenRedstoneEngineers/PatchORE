package org.openredstone.patch;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.openredstone.PatchORE;

import java.util.Map;

public class EnchantmentPatch extends Patch implements Listener {

    public EnchantmentPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem().getEnchantments().size() == 0) {
            return;
        }

        ItemStack item = event.getItem();
        Map<Enchantment, Integer> enchants = item.getEnchantments();
        boolean sendWarning = false;
        for (Enchantment key : enchants.keySet()) {
            int minimum = PatchORE.config.getInt("enchantments." + key.getKey().getKey() + ".minimum_enchantment");
            int maximum = PatchORE.config.getInt("enchantments." + key.getKey().getKey() + ".maximum_enchantment");
            int enchantPower = enchants.get(key);
            if (enchantPower > maximum) {
                item.removeEnchantment(key);
                item.addUnsafeEnchantment(key, maximum);
                sendWarning = true;
            } else if (enchantPower < minimum) {
                item.removeEnchantment(key);
                item.addUnsafeEnchantment(key, minimum);
                sendWarning = true;
            }
        }
        if (sendWarning) {
            sendMessage(event.getPlayer(), "We just filtOREd your enchantments :o");
        }
    }
}
