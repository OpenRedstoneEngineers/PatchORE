package org.openredstone.patch;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignPatch extends Patch implements Listener {

    public CommandSignPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent event) {
        Block bl = event.getBlock();

        if (!Tag.SIGNS.getValues().contains(bl.getType())) return;

        ItemStack sign = CraftItemStack.asNMSCopy(event.getItemInHand());

        if (!sign.hasTag()) return;

        NBTTagCompound tag = sign.getTag().getCompound("BlockEntityTag");

        if (tag.getString("Text1").contains("run_command")
                || tag.getString("Text2").contains("run_command")
                || tag.getString("Text3").contains("run_command")
                || tag.getString("Text4").contains("run_command")) {
            sendMessage(event.getPlayer(), "Yeah, no command signs.");
            event.setCancelled(true);
        }
    }
}